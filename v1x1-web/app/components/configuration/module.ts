import {Component, EventEmitter, Input, Output} from '@angular/core';
import {V1x1Module} from "../../model/v1x1_module";
import {V1x1ConfigurationSet} from "../../model/v1x1_configuration_set";
import {V1x1Tenant} from "../../model/v1x1_tenant";

@Component({
  selector: 'configuration-module',
  template: `
    <ngb-tabset class="tabs-left">
      <ngb-tab [title]="'Everything' + (configurationSet.tenant.dirty() ? '*' : '')">
        <ng-template ngbTabContent>
          <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.user" [(originalConfiguration)]="configurationSet.tenant.originalConfiguration" [(configuration)]="configurationSet.tenant.configuration" [activeTenant]="activeTenant" [activeChannelGroup]="null" [activeChannel]="null" [enabled]="true" [originalEnabled]="true" [scope]="'tenant'"></configuration-scope>
        </ng-template>
      </ngb-tab>
      <div *ngFor="let channelGroup of configurationSet.channelGroups; let i = index">
        <ngb-tab>
          <ng-template ngbTabTitle>
            <platform-formatter [platform]="channelGroup.channelGroup.platform">{{channelGroup.channelGroup.displayName}}{{channelGroup.config.dirty() ? '*' : ''}}</platform-formatter>
          </ng-template>
          <ng-template ngbTabContent>
            <configuration-channel-group [(v1x1Module)]="v1x1Module" [(activeTenant)]="activeTenant" [(activeChannelGroup)]="configurationSet.channelGroups[i]"></configuration-channel-group>
          </ng-template>
        </ngb-tab>
      </div>
    </ngb-tabset>
  `
})
export class ConfigurationModuleComponent {
  public v1x1ModuleValue: V1x1Module;
  @Input() public configurationSet: V1x1ConfigurationSet;
  @Input() public activeTenant: V1x1Tenant;
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
