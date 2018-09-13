import {ConfigurableComponent} from "../configurable";
import {Component, Input, OnChanges, OnInit, SimpleChanges} from "@angular/core";
import {Observable} from "rxjs/index";
import {V1x1ConfigurationDefinitionField} from "../../../model/api/v1x1_configuration_definition_field";
import {V1x1GlobalState} from "../../../services/global_state";
import {map} from "rxjs/operators";
import {V1x1ChannelGroupPlatformGroup} from "../../../model/api/v1x1_channel_group_platform_group";
import {Tenant} from "../../../model/state/tenant";

@Component({
  selector: 'configuration-field-value-platform-group',
  template: `
    <mat-form-field>
      <mat-select placeholder="Platform Group" [(value)]="configuration">
        <mat-option *ngFor="let group of groups | async" [value]="group.name">
          {{group.displayName}}
        </mat-option>
      </mat-select>
    </mat-form-field>
    <span class="input-group-append" *ngIf="configDirty()">
      <button mat-button color="accent" (click)="abandonChanges()"><i class="far fa-undo"></i></button>
    </span>
`
})
export class ConfigurationFieldValuePlatformGroupComponent extends ConfigurableComponent implements OnInit, OnChanges {
  @Input() public field: V1x1ConfigurationDefinitionField;
  groups: Observable<V1x1ChannelGroupPlatformGroup[]> = null;

  constructor(private globalState: V1x1GlobalState) {
    super();
  }

  ngOnInit(): void {
    this.reconfigure();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.reconfigure();
  }

  reconfigure(): void {
    this.groups = this.globalState.webapp.currentTenant.pipe(
      map((activeTenant: Tenant) => activeTenant.channelGroups.get(this.channelGroup.platform + ":" + this.channelGroup.id).platformGroups)
    );
  }
}
