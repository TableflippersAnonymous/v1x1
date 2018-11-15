import {Component, OnDestroy, OnInit} from '@angular/core';
import {Module} from "../../model/state/module";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1PubSub} from "../../services/pubsub";
import {Observable} from "rxjs";

@Component({
  selector: 'configuration-page',
  template: `
    <mat-tab-group>
      <mat-tab *ngFor="let module of (modules | async | keyvalue)">
        <ng-template mat-tab-label>{{module.value.displayName + (module.dirty() ? '*' : '')}}</ng-template>
        <configuration-module [v1x1Module]="module.value"></configuration-module>
      </mat-tab>
    </mat-tab-group>
  `
})
export class ConfigurationPageComponent implements OnInit, OnDestroy {
  public v1x1Modules: Module[] = [];
  public modules: Observable<Map<string, Module>>;

  constructor(private globalState: V1x1GlobalState, private pubsub: V1x1PubSub) {

  }

  ngOnInit() {
    this.modules = this.globalState.webapp.modules;
  }

  ngOnDestroy() {
  }
  /*
  this.pubsubSubscription = this.pubsub.topic("topic:" + tenant.id + ":api:config").pipe(map(r => JsonConvert.deserializeObject(r, V1x1ConfigChange))).subscribe(
        (configChange: V1x1ConfigChange) => {
          this.subscriptions.push(this.api.getTenantConfigurationSet(tenant.id, configChange.module).subscribe(
            config => {
              this.configurationSets[this.v1x1Modules.findIndex(module => module.name === configChange.module)].setOriginal(config);
            }
          ));
        }
      );
   */
}
