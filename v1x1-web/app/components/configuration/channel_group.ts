import {Component, EventEmitter, Input, Output} from '@angular/core';
import {V1x1Module} from "../../model/v1x1_module";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {V1x1ChannelGroupConfigurationWrapper} from "../../model/v1x1_channel_group_configuration_wrapper";

@Component({
  selector: 'configuration-channel-group',
  template: `
    <mat-tab-group>
      <mat-tab>
        <ng-template mat-tab-label>
          Everything{{activeChannelGroup.config.channelGroup.dirty() ? '*' : ''}}
        </ng-template>
        <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.user" [(originalConfiguration)]="activeChannelGroup.config.channelGroup.originalConfiguration" [(configuration)]="activeChannelGroup.config.channelGroup.configuration" [activeTenant]="activeTenant" [activeChannelGroup]="activeChannelGroup.channelGroup" [activeChannel]="null" [(enabled)]="activeChannelGroup.config.channelGroup.enabled" [(originalEnabled)]="activeChannelGroup.config.channelGroup.originalEnabled" [scope]="'channelGroup'"></configuration-scope>
      </mat-tab>
      <mat-tab *ngFor="let channel of activeChannelGroup.config.channels; let i = index">
        <ng-template mat-tab-label>
          <platform-formatter [platform]="activeChannelGroup.channelGroup.platform">{{channel.channel.displayName}}{{channel.config.dirty() ? '*' : ''}}</platform-formatter>
        </ng-template>
        <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.user" [(originalConfiguration)]="activeChannelGroup.config.channels[i].config.originalConfiguration" [(configuration)]="activeChannelGroup.config.channels[i].config.configuration" [activeTenant]="activeTenant" [activeChannelGroup]="activeChannelGroup.channelGroup" [activeChannel]="activeChannelGroup.config.channels[i].channel" [(enabled)]="activeChannelGroup.config.channels[i].config.enabled" [(originalEnabled)]="activeChannelGroup.config.channels[i].config.originalEnabled" [scope]="'channel'"></configuration-scope>
      </mat-tab>
    </mat-tab-group>
  `
})
export class ConfigurationChannelGroupComponent {
  public v1x1ModuleValue: V1x1Module;
  @Input() public activeTenant: V1x1Tenant;
  @Input() public activeChannelGroup: V1x1ChannelGroupConfigurationWrapper;
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
