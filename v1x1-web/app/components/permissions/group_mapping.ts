import {Component, Input} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1ConfigurationDefinition} from "../../model/v1x1_configuration_definition";
import {JsonConvert} from "json2typescript/index";
import {V1x1ChannelGroupPlatformMappingWrapper} from "../../model/v1x1_channel_group_platform_mapping_wrapper";

@Component({
  selector: 'permissions-group-mapping-page',
  template: `
    <div class="card-container">
      <form>
        <mat-card>
          <mat-card-header>
            <mat-card-title><h1>Group Mappings</h1></mat-card-title>
            <mat-card-subtitle>This lets you automatically assign groups to users</mat-card-subtitle>
            <mat-card-subtitle>Here you can assign v1x1 groups to users with certain Twitch badges or Discord roles.</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <configuration-field *ngFor="let field of configurationDefinition.fields"
                                 [field]="field" [complexFields]="configurationDefinition.complexFields"
                                 [originalConfiguration]="originalConfiguration[field.jsonField]"
                                 [configuration]="configuration[field.jsonField]" (configurationChange)="setConfigField(field.jsonField, $event)"></configuration-field>
          </mat-card-content>
          <mat-card-actions>
            <button class="btn btn-primary" *ngIf="configDirty()" (click)="saveChanges()">Save Changes</button>
            <button class="btn btn-secondary" *ngIf="configDirty()" (click)="abandonChanges()">Abandon Changes</button>
          </mat-card-actions>
        </mat-card>
      </form>
    </div>
  `
})
export class PermissionsGroupMappingComponent {
  channelGroupPlatformMappingValue: V1x1ChannelGroupPlatformMappingWrapper;
  originalConfiguration: Object = {};
  configuration: Object = {};
  configSet: boolean = false;
  configurationDefinition: V1x1ConfigurationDefinition = JsonConvert.deserializeObject({
    display_name: "Group Mapping",
    tenant_permission: "READ_WRITE",
    fields: [
      {
        display_name: "Mappings",
        description: "Group Mappings",
        default_value: "null",
        config_type: "COMPLEX_LIST",
        requires: [],
        tenant_permission: "READ_WRITE",
        json_field: "mappings",
        complex_type: "mapping"
      }
    ],
    complex_fields: {
      mapping: [
        {
          display_name: "Platform Group",
          description: "This is the group name on the third-party.  On Twitch, this is upper-case badge names.  On Discord, this is role IDs.",
          default_value: "null",
          config_type: "STRING",
          requires: [],
          tenant_permission: "READ_WRITE",
          json_field: "platformGroup",
          complex_type: null
        },
        {
          display_name: "Group ID",
          description: "This is the v1x1 group ID.",
          default_value: "null",
          config_type: "STRING",
          requires: [],
          tenant_permission: "READ_WRITE",
          json_field: "groupId",
          complex_type: null
        }
      ]
    }
  }, V1x1ConfigurationDefinition);

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api, private globalState: V1x1GlobalState) {}

  @Input()
  set channelGroupPlatformMapping(channelGroupPlatformMapping: V1x1ChannelGroupPlatformMappingWrapper) {
    this.channelGroupPlatformMappingValue = channelGroupPlatformMapping;
    if(!this.configSet) {
      this.configuration = {
        mappings: JSON.parse(JSON.stringify(this.channelGroupPlatformMapping.mappings))
      };
      this.configSet = true;
      console.log(this.configuration);
    }
    this.originalConfiguration = JSON.parse(JSON.stringify({
      mappings: JSON.parse(JSON.stringify(this.channelGroupPlatformMapping.mappings))
    }));
  }

  get channelGroupPlatformMapping() {
    return this.channelGroupPlatformMappingValue;
  }

  setConfigField(field: string, event) {
    console.log("Update " + field + " " + JSON.stringify(event));
    this.configuration[field] = event;
  }

  configDirty(): boolean {
    return JSON.stringify(this.configuration) !== JSON.stringify(this.originalConfiguration);
  }

  saveChanges(): void {

  }

  abandonChanges(): void {
    this.configuration = JSON.parse(JSON.stringify(this.originalConfiguration));
  }

}
