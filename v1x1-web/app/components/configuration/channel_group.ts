import {Component, Input} from '@angular/core';
import {Module} from "../../model/state/module";
import {ChannelGroup} from "../../model/state/channel_group";

@Component({
  selector: 'configuration-channel-group',
  template: `
    <mat-tab-group>
      <mat-tab>
        <ng-template mat-tab-label>
          Everything{{activeChannelGroup.moduleConfiguration.get(v1x1Module.name).dirty() ? '*' : ''}}
        </ng-template>
        <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.user" [originalConfiguration]="activeChannelGroup.moduleConfiguration.get(v1x1Module.name).originalConfiguration" [configuration]="activeChannelGroup.moduleConfiguration.get(v1x1Module.name).configuration" [activeTenant]="activeChannelGroup.tenant" [activeChannelGroup]="activeChannelGroup" [activeChannel]="null" [(enabled)]="activeChannelGroup.moduleConfiguration.get(v1x1Module.name).enabled" [(originalEnabled)]="activeChannelGroup.moduleConfiguration.get(v1x1Module.name).originalEnabled" [scope]="'channelGroup'"></configuration-scope>
      </mat-tab>
      <mat-tab *ngFor="let channel of activeChannelGroup.channels | keyvalue">
        <ng-template mat-tab-label>
          <platform-formatter [platform]="activeChannelGroup.platform">{{channel.value.displayName}}{{channel.value.moduleConfiguration.get(v1x1Module.name).dirty() ? '*' : ''}}</platform-formatter>
        </ng-template>
        <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.user" [originalConfiguration]="channel.value.moduleConfiguration.get(v1x1Module.name).originalConfiguration" [configuration]="channel.value.moduleConfiguration.get(v1x1Module.name).configuration" [activeTenant]="activeChannelGroup.tenant" [activeChannelGroup]="activeChannelGroup" [activeChannel]="channel.value" [(enabled)]="channel.value.moduleConfiguration.get(v1x1Module.name).enabled" [(originalEnabled)]="channel.value.moduleConfiguration.get(v1x1Module.name).originalEnabled" [scope]="'channel'"></configuration-scope>
      </mat-tab>
    </mat-tab-group>
  `
})
export class ConfigurationChannelGroupComponent {
  @Input() public v1x1Module: Module;
  @Input() public activeChannelGroup: ChannelGroup;
}
