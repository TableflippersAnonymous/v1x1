import {Component} from '@angular/core';
import {V1x1Module} from "../../model/v1x1_module";
import {Permission} from "../../model/v1x1_configuration_definition_field";
import {V1x1Api} from "../../services/api";

@Component({
  selector: 'configuration-page',
  template: `<ngb-tabset class="tabs-left">
    <div *ngFor="let v1x1Module of v1x1Modules; let i = index">
      <ngb-tab *ngIf="(v1x1Module.configurationDefinitionSet.global !== null && v1x1Module.configurationDefinitionSet.global.tenantPermission !== permissions.NONE)
                   || (v1x1Module.configurationDefinitionSet.tenant !== null && v1x1Module.configurationDefinitionSet.tenant.tenantPermission !== permissions.NONE)
                   || (v1x1Module.configurationDefinitionSet.channel !== null && v1x1Module.configurationDefinitionSet.channel.tenantPermission !== permissions.NONE)"
               [title]="v1x1Module.displayName + (v1x1Module.dirty() ? '*' : '')">
        <template ngbTabContent>
          <configuration-module [(v1x1Module)]="v1x1Modules[i]"></configuration-module>
        </template>
      </ngb-tab>
    </div>
  </ngb-tabset>
  <div>
    {{json.stringify(v1x1Modules.map(mapper))}}
  </div>
`
})
export class ConfigurationPageComponent {
  /* This will eventually be pulled from the API. */
  public v1x1Modules: V1x1Module[];
  public permissions = Permission;
  public json = JSON;

  constructor(v1x1Api: V1x1Api) {
    this.v1x1Modules = v1x1Api.getModules();
  }

  public mapper = function(m: V1x1Module, _idx: number, _ary: V1x1Module[]) { return [m.name, m.configurationSet] };
}
