import {Component, Input} from '@angular/core';
import {V1x1Module} from "../model/v1x1_module";

@Component({
  selector: 'configuration-module',
  template: `
<b>{{v1x1Module.displayName}}</b>
<p>Description: {{v1x1Module.description}}</p>
<hr>
<ngb-tabset>
  <ngb-tab title="Global" *ngIf="v1x1Module.configurationDefinitionSet.global != null">
    <template ngbTabContent>
      <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.global" [configuration]="{}" [id]="null" [scope]="'global'"></configuration-scope>
    </template>
  </ngb-tab>
  <ngb-tab title="Tenant" *ngIf="v1x1Module.configurationDefinitionSet.tenant != null">
    <template ngbTabContent>
      <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.tenant" [configuration]="{}" [id]="null" [scope]="'tenant'"></configuration-scope>
    </template>
  </ngb-tab>
  <ngb-tab title="Channel" *ngIf="v1x1Module.configurationDefinitionSet.channel != null">
    <template ngbTabContent>
      <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.channel" [configuration]="{}" [id]="null" [scope]="'channel'"></configuration-scope>
    </template>
  </ngb-tab>
</ngb-tabset>
`
})
export class ConfigurationModuleComponent {
  @Input() public v1x1Module: V1x1Module;
}
