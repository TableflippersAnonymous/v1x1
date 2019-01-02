import {ConfigurableComponent} from "../configurable";
import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from "@angular/core";
import {V1x1Api} from "../../../services/api";
import {V1x1ApiCache} from "../../../services/api_cache";
import {Subscription} from "rxjs/index";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {V1x1GlobalState} from "../../../services/global_state";
import {map, mergeAll} from "rxjs/operators";
import {V1x1ChannelGroupPlatformGroup} from "../../../model/v1x1_channel_group_platform_group";

@Component({
  selector: 'configuration-field-value-platform-group',
  template: `
    <mat-form-field>
      <mat-select placeholder="Platform Group" [(value)]="configuration">
        <mat-option *ngFor="let group of groups" [value]="group.name">
          {{group.displayName}}
        </mat-option>
      </mat-select>
    </mat-form-field>
    <span class="input-group-append" *ngIf="configDirty()">
      <button mat-button color="accent" (click)="abandonChanges()"><i class="far fa-undo"></i></button>
    </span>
`
})
export class ConfigurationFieldValuePlatformGroupComponent extends ConfigurableComponent implements OnInit, OnDestroy, OnChanges {
  @Input() public field: V1x1ConfigurationDefinitionField;
  groups: V1x1ChannelGroupPlatformGroup[] = [];
  private subscription: Subscription = null;

  constructor(private api: V1x1Api, private apiCache: V1x1ApiCache, private globalState: V1x1GlobalState) {
    super();
  }

  ngOnInit(): void {
    this.reconfigure();
  }

  ngOnDestroy(): void {
    if(this.subscription != null)
      this.subscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.reconfigure();
  }

  reconfigure(): void {
    if(this.subscription != null)
      this.subscription.unsubscribe();
    this.subscription = this.globalState.activeTenant.get().pipe(map(activeTenant =>
      this.apiCache.getPlatformGroups(activeTenant.id, this.activeChannelGroup.platform, this.activeChannelGroup.id)),
      mergeAll()).subscribe(
      groups => {
        this.groups = groups;
      }
    );
  }
}
