import {Component, OnDestroy, OnInit} from '@angular/core';
import {Module} from "../../model/state/module";
import {Permission} from "../../model/api/v1x1_configuration_definition_field";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1Tenant} from "../../model/api/v1x1_tenant";
import {V1x1ConfigurationSet} from "../../model/api/v1x1_configuration_set";
import {V1x1Api} from "../../services/api";
import {JsonConvert} from "json2typescript";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1PubSub} from "../../services/pubsub";
import {V1x1ConfigChange} from "../../model/api/v1x1_config_change";
import {first, map} from "rxjs/operators";
import {forkJoin, Subscription} from "rxjs";

@Component({
  selector: 'configuration-page',
  template: `
    <mat-tab-group>
      <mat-tab *ngFor="let v1x1Module of v1x1Modules; let i = index">
        <ng-template mat-tab-label>{{v1x1Module.displayName + (v1x1Module.dirty(configurationSets[i]) ? '*' : '')}}</ng-template>
        <configuration-module [(v1x1Module)]="v1x1Modules[i]" [(configurationSet)]="configurationSets[i]" [activeTenant]="activeTenantValue" *ngIf="configurationSets[i]"></configuration-module>
      </mat-tab>
    </mat-tab-group>
  `
})
export class ConfigurationPageComponent implements OnInit, OnDestroy {
  public v1x1Modules: Module[] = [];
  public permissions = Permission;
  public json = JSON;
  public activeTenantValue: V1x1Tenant = undefined;
  public configurationSets: V1x1ConfigurationSet[] = [];
  private subscriptions: Subscription[] = [];
  private pubsubSubscription: Subscription = null;

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api, private globalState: V1x1GlobalState, private pubsub: V1x1PubSub) {

  }

  ngOnInit() {
    this.subscriptions.push(this.cachedApi.getModules().pipe(first()).subscribe(modules => {
      this.v1x1Modules = modules
        .filter((v1x1Module: Module) => v1x1Module.configurationDefinitionSet.user !== null)
        .filter((v1x1Module: Module) => v1x1Module.configurationDefinitionSet.user.tenantPermission !== Permission.NONE);
      this.recalculateTenantConfiguration();
    }));
    this.subscriptions.push(this.globalState.activeTenant.get().subscribe(tenant => {
      this.activeTenantValue = tenant;
      this.recalculateTenantConfiguration();
    }));
  }

  ngOnDestroy() {
    this.subscriptions.forEach(
      sub => sub.unsubscribe()
    );
    if(this.pubsubSubscription != null)
      this.pubsubSubscription.unsubscribe();
  }

  recalculateTenantConfiguration() {
    if(this.v1x1Modules === [] || this.activeTenantValue === undefined)
      return;
    let tenant = this.activeTenantValue;
    this.subscriptions.push(forkJoin(this.v1x1Modules.map(
      v1x1Module => this.api.getTenantConfigurationSet(tenant.id, v1x1Module.name)
    )).subscribe(configs => {
      if(this.pubsubSubscription != null)
        this.pubsubSubscription.unsubscribe();
      this.configurationSets = configs;
      this.pubsubSubscription = this.pubsub.topic("topic:" + tenant.id + ":api:config").pipe(map(r => JsonConvert.deserializeObject(r, V1x1ConfigChange))).subscribe(
        (configChange: V1x1ConfigChange) => {
          this.subscriptions.push(this.api.getTenantConfigurationSet(tenant.id, configChange.module).subscribe(
            config => {
              this.configurationSets[this.v1x1Modules.findIndex(module => module.name === configChange.module)].setOriginal(config);
            }
          ));
        }
      );
    }));
  }
}
