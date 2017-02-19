import {Component, Input} from "@angular/core";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {V1x1ConfigurationDefinition} from "../../model/v1x1_configuration_definition";
import {JsonConvert} from "json2typescript";
import {V1x1GroupMembership} from "../../model/v1x1_group_membership";
@Component({
  selector: 'permissions-group-page',
  template: `
    <div class="jumbotron" style="margin: 1rem;">
      <h1 class="display-3">{{group.group.name}}</h1>
      <p class="lead">Group ID: {{group.group.groupId}}</p>
      <hr class="my-4">
      <p>Additional help can be found in the help pages.</p>
    </div>
    <form>
      <configuration-field *ngFor="let field of configurationDefinition.fields"
                           [field]="field" [complexFields]="configurationDefinition.complexFields"
                           [originalConfiguration]="originalConfiguration[field.jsonField]"
                           [configuration]="configuration[field.jsonField]" (configurationChange)="setConfigField(field.jsonField, $event)"></configuration-field>
    </form>
  `
})
export class PermissionsGroupComponent {
  @Input() activeTenant: V1x1Tenant;
  @Input() groupValue: V1x1GroupMembership;
  originalConfiguration: Object = {};
  configuration: Object = {};
  configSet: boolean = false;
  configurationDefinition: V1x1ConfigurationDefinition = JsonConvert.deserializeObject({
    tenant_permission: "READ_WRITE",
    fields: [
      {
        display_name: "Permissions",
        description: "List of permissions this group has",
        default_value: "null",
        config_type: "STRING_LIST",
        requires: [],
        tenant_permission: "READ_WRITE",
        json_field: "permissions",
        complex_type: null
      },
      {
        display_name: "Users",
        description: "List of users in this group",
        default_value: "null",
        config_type: "STRING_LIST",
        requires: [],
        tenant_permission: "READ_WRITE",
        json_field: "users",
        complex_type: null
      }
    ],
    complex_fields: []
  }, V1x1ConfigurationDefinition);

  @Input()
  set group(group: V1x1GroupMembership) {
    this.groupValue = group;
    if(!this.configSet) {
      this.configuration = {
        permissions: this.group.group.permissions,
        users: this.group.members.map(user => user.id)
      };
      this.configSet = true;
    }
    this.originalConfiguration = JSON.parse(JSON.stringify({
      permissions: this.group.group.permissions,
      users: this.group.members.map(user => user.id)
    }));
  }

  get group() {
    return this.groupValue;
  }

  setConfigField(field: string, event) {

  }
}
