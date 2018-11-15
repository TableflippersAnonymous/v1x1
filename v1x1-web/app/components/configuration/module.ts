import {Component, Input, OnInit} from '@angular/core';
import {Module} from "../../model/state/module";
import {V1x1GlobalState} from "../../services/global_state";
import {map} from "rxjs/operators";
import {Configuration} from "../../model/state/configuration";
import {Observable} from "rxjs";
import {ChannelGroup} from "../../model/state/channel_group";

@Component({
  selector: 'configuration-module',
  template: `
    <mat-tab-group>
      <mat-tab>
        <ng-template mat-tab-label>Everything{{(configuration | async).dirty() ? '*' : ''}}</ng-template>
        <configuration-scope [v1x1Module]="v1x1Module" [configurationDefinition]="v1x1Module.configurationDefinitionSet.user" [originalConfiguration]="(configuration | async).originalConfiguration" [(configuration)]="(configuration | async).configuration" [activeChannelGroup]="null" [activeChannel]="null" [enabled]="true" [originalEnabled]="true" [scope]="'tenant'"></configuration-scope>
      </mat-tab>
      <mat-tab *ngFor="let channelGroup of channelGroups | async">
        <ng-template mat-tab-label>
          <platform-formatter [platform]="channelGroup.platform">{{channelGroup.displayName}}{{channelGroup.moduleConfiguration.get(v1x1Module.name).dirty() ? '*' : ''}}</platform-formatter>
        </ng-template>
        <configuration-channel-group [v1x1Module]="v1x1Module" [activeChannelGroup]="channelGroup"></configuration-channel-group>
      </mat-tab>
    </mat-tab-group>
  `
})
export class ConfigurationModuleComponent implements OnInit {
  @Input()
  public v1x1Module: Module;
  public configuration: Observable<Configuration>;
  public channelGroups: Observable<ChannelGroup[]>;

  constructor(private globalState: V1x1GlobalState) {}

  ngOnInit() {
    this.configuration = this.globalState.webapp.currentTenant.pipe(
      map(tenant => tenant.moduleConfiguration.get(this.v1x1Module.name))
    );
    this.channelGroups = this.globalState.webapp.currentTenant.pipe(
      map(tenant => Array.from(tenant.channelGroups.values()))
    );
  }
}
