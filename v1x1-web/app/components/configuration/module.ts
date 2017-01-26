import {Component, Input, Output, EventEmitter} from '@angular/core';
import {V1x1Module} from "../../model/v1x1_module";

@Component({
  selector: 'configuration-module',
  template: `
    <div class="jumbotron" style="margin: 1rem;">
      <h1 class="display-3">{{v1x1Module.displayName}}</h1>
      <p class="lead">{{v1x1Module.description}}</p>
      <hr class="my-4">
      <p>Additional help can be found in the help pages.</p>
    </div>
    <ngb-tabset>
      <ngb-tab [title]="'Global' + (v1x1Module.configurationSet.global.dirty() ? '*' : '')" *ngIf="v1x1Module.configurationDefinitionSet.global !== null && v1x1Module.configurationDefinitionSet.global.tenantPermission !== NONE">
        <template ngbTabContent>
          <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.global" [originalConfiguration]="v1x1ModuleValue.configurationSet.global.originalConfiguration" [(configuration)]="v1x1ModuleValue.configurationSet.global.configuration" [id]="null" [scope]="'global'"></configuration-scope>
        </template>
      </ngb-tab>
      <ngb-tab [title]="'Tenant' + (v1x1Module.configurationSet.tenant.dirty() ? '*' : '')" *ngIf="v1x1Module.configurationDefinitionSet.tenant !== null && v1x1Module.configurationDefinitionSet.tenant.tenantPermission !== NONE">
        <template ngbTabContent>
          <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.tenant" [originalConfiguration]="v1x1ModuleValue.configurationSet.tenant.originalConfiguration" [(configuration)]="v1x1ModuleValue.configurationSet.tenant.configuration" [id]="null" [scope]="'tenant'"></configuration-scope>
        </template>
      </ngb-tab>
      <ngb-tab [title]="'Channel' + (v1x1Module.configurationSet.channel.dirty() ? '*' : '')" *ngIf="v1x1Module.configurationDefinitionSet.channel !== null && v1x1Module.configurationDefinitionSet.channel.tenantPermission !== NONE">
        <template ngbTabContent>
          <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.channel" [originalConfiguration]="v1x1ModuleValue.configurationSet.tenant.originalConfiguration" [(configuration)]="v1x1ModuleValue.configurationSet.channel.configuration" [id]="null" [scope]="'channel'"></configuration-scope>
        </template>
      </ngb-tab>
    </ngb-tabset>
  `
})
export class ConfigurationModuleComponent {
  public v1x1ModuleValue: V1x1Module;
  @Output() public v1x1ModuleChange = new EventEmitter();
  get v1x1Module() {
    return this.v1x1ModuleValue;
  }
  @Input()
  set v1x1Module(val) {
    this.v1x1ModuleValue = val;
    this.v1x1ModuleChange.emit(this.v1x1ModuleValue);
  }
}
