import {Component, Input} from '@angular/core';
import {V1x1Module} from "../../model/v1x1_module";
import {Permission} from "../../model/v1x1_configuration_definition_field";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {V1x1ConfigurationSet} from "../../model/v1x1_configuration_set";
import {V1x1Api} from "../../services/api";
import {Observable} from "rxjs";
import {V1x1Configuration} from "../../model/v1x1_configuration";
import {V1x1GlobalState} from "../../services/global_state";

@Component({
  selector: 'configuration-page',
  template: `<ngb-tabset class="tabs-left">
    <div *ngFor="let v1x1Module of v1x1Modules; let i = index">
      <ngb-tab *ngIf="(v1x1Module.configurationDefinitionSet.global !== null && v1x1Module.configurationDefinitionSet.global.tenantPermission !== permissions.NONE)
                   || (v1x1Module.configurationDefinitionSet.tenant !== null && v1x1Module.configurationDefinitionSet.tenant.tenantPermission !== permissions.NONE)
                   || (v1x1Module.configurationDefinitionSet.channel !== null && v1x1Module.configurationDefinitionSet.channel.tenantPermission !== permissions.NONE)"
               [title]="v1x1Module.displayName + (v1x1Module.dirty(configurationSets[i]) ? '*' : '')">
        <template ngbTabContent>
          <configuration-module [(v1x1Module)]="v1x1Modules[i]" [(configurationSet)]="configurationSets[i]" [activeTenant]="activeTenantValue" *ngIf="configurationSets[i]"></configuration-module>
        </template>
      </ngb-tab>
    </div>
  </ngb-tabset>
`
})
export class ConfigurationPageComponent {
  /* This will eventually be pulled from the API. */
  public v1x1Modules: V1x1Module[] = [];
  public permissions = Permission;
  public json = JSON;
  public activeTenantValue: V1x1Tenant = null;
  public configurationSets: V1x1ConfigurationSet[] = [];

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api, private globalState: V1x1GlobalState) {
    this.cachedApi.getModules().subscribe(modules => {
      this.v1x1Modules = modules;
      this.recalculateTenantConfiguration();
    });
    this.globalState.activeTenant.get().subscribe(tenant => {
      this.activeTenantValue = tenant;
      this.recalculateTenantConfiguration();
    });
  }

  recalculateTenantConfiguration() {
    if(this.v1x1Modules === [] || this.activeTenantValue === null)
      return;
    Observable.forkJoin(this.v1x1Modules.map(
      v1x1Module => this.api.getTenantConfiguration(this.activeTenantValue.id, v1x1Module.name)
    )).map(configs => configs.map(config => new V1x1ConfigurationSet(new V1x1Configuration({}), config, new V1x1Configuration({}))))
      .subscribe(configs => this.configurationSets = configs);
  }

  debugConfig() {
    return JSON.stringify(this.v1x1Modules.map((m, idx) => [m.name, this.configurationSets && this.configurationSets[idx]]));
  }
}
