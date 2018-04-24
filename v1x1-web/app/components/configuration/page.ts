import {Component, OnDestroy, OnInit} from '@angular/core';
import {V1x1Module} from "../../model/v1x1_module";
import {Permission} from "../../model/v1x1_configuration_definition_field";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {V1x1ConfigurationSet} from "../../model/v1x1_configuration_set";
import {V1x1Api} from "../../services/api";
import {Observable} from "rxjs";
import {JsonConvert} from "json2typescript";
import {V1x1GlobalState} from "../../services/global_state";
import {Subscription} from "rxjs/Subscription";
import {V1x1PubSub} from "../../services/pubsub";
import {V1x1ConfigChange} from "../../model/v1x1_config_change";

@Component({
  selector: 'configuration-page',
  template: `<ngb-tabset class="tabs-left">
    <div *ngFor="let v1x1Module of v1x1Modules; let i = index">
      <ngb-tab *ngIf="v1x1Module.configurationDefinitionSet.user !== null && v1x1Module.configurationDefinitionSet.user?.tenantPermission !== permissions.NONE"
               [title]="v1x1Module.displayName + (v1x1Module.dirty(configurationSets[i]) ? '*' : '')">
        <template ngbTabContent>
          <configuration-module [(v1x1Module)]="v1x1Modules[i]" [(configurationSet)]="configurationSets[i]" [activeTenant]="activeTenantValue" *ngIf="configurationSets[i]"></configuration-module>
        </template>
      </ngb-tab>
    </div>
  </ngb-tabset>
`
})
export class ConfigurationPageComponent implements OnInit, OnDestroy {
  /* This will eventually be pulled from the API. */
  public v1x1Modules: V1x1Module[] = [];
  public permissions = Permission;
  public json = JSON;
  public activeTenantValue: V1x1Tenant = undefined;
  public configurationSets: V1x1ConfigurationSet[] = [];
  private subscriptions: Subscription[] = [];
  private pubsubSubscription: Subscription = null;

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api, private globalState: V1x1GlobalState, private pubsub: V1x1PubSub) {

  }

  ngOnInit() {
    this.subscriptions.push(this.cachedApi.getModules().first().subscribe(modules => {
      this.v1x1Modules = modules;
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
    this.subscriptions.push(Observable.forkJoin(this.v1x1Modules.map(
      v1x1Module => this.api.getTenantConfigurationSet(tenant.id, v1x1Module.name)
    )).subscribe(configs => {
      if(this.pubsubSubscription != null)
        this.pubsubSubscription.unsubscribe();
      this.configurationSets = configs;
      this.pubsubSubscription = this.pubsub.topic("topic:" + tenant.id + ":api:config").map(r => JsonConvert.deserializeObject(r, V1x1ConfigChange)).subscribe(
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

  debugConfig() {
    return JSON.stringify(this.v1x1Modules.map((m, idx) => [m.name, this.configurationSets && this.configurationSets[idx]]));
  }
}
