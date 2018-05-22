import {Component, EventEmitter, Input, Output} from "@angular/core";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {V1x1ConfigurationDefinition} from "../../model/v1x1_configuration_definition";
import {JsonConvert} from "json2typescript";
import {V1x1GroupMembership} from "../../model/v1x1_group_membership";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";

@Component({
  selector: 'permissions-group-page',
  template: `
    <div class="card-container">
      <form>
        <mat-card>
          <mat-card-header>
            <mat-card-title><h1>{{group.group.name}}</h1></mat-card-title>
            <mat-card-subtitle>Group ID: {{group.group.groupId}}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <configuration-field *ngFor="let field of configurationDefinition.fields"
                                 [field]="field" [complexFields]="configurationDefinition.complexFields"
                                 [originalConfiguration]="originalConfiguration[field.jsonField]" [activeTenant]="activeTenant"
                                 [configuration]="configuration[field.jsonField]" (configurationChange)="setConfigField(field.jsonField, $event)"></configuration-field>
          </mat-card-content>
          <mat-card-actions>
            <button mat-raised-button color="primary" *ngIf="configDirty()" (click)="saveChanges()">Save Changes</button>
            <button mat-raised-button color="accent" *ngIf="configDirty()" (click)="abandonChanges()">Abandon Changes</button>
            <button mat-raised-button color="warn" (click)="deleteGroup()">Delete Group</button>
          </mat-card-actions>
        </mat-card>
      </form>
    </div>
  `
})
export class PermissionsGroupComponent {
  @Input() activeTenant: V1x1Tenant;
  @Input() groupValue: V1x1GroupMembership;
  @Output() update: EventEmitter<boolean> = new EventEmitter<boolean>();
  originalConfiguration: Object = {};
  configuration: Object = {};
  configSet: boolean = false;
  configurationDefinition: V1x1ConfigurationDefinition = JsonConvert.deserializeObject({
    display_name: "Permission Group",
    tenant_permission: "READ_WRITE",
    fields: [
      {
        display_name: "Permissions",
        description: "List of permissions this group has",
        default_value: "null",
        config_type: "PERMISSION_LIST",
        requires: [],
        tenant_permission: "READ_WRITE",
        json_field: "permissions",
        complex_type: null
      },
      {
        display_name: "Users",
        description: "List of users in this group",
        default_value: "null",
        config_type: "USER_LIST",
        requires: [],
        tenant_permission: "READ_WRITE",
        json_field: "users",
        complex_type: null
      }
    ],
    complex_fields: []
  }, V1x1ConfigurationDefinition);

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api) {}

  @Input()
  set group(group: V1x1GroupMembership) {
    this.groupValue = group;
    if(!this.configSet) {
      this.configuration = {
        permissions: this.group.group.permissions,
        users: this.group.members
      };
      this.configSet = true;
    }
    this.originalConfiguration = JSON.parse(JSON.stringify({
      permissions: this.group.group.permissions,
      users: this.group.members
    }));
  }

  get group() {
    return this.groupValue;
  }

  setConfigField(field: string, event) {
    console.log("Update " + field + " " + JSON.stringify(event));
    this.configuration[field] = event;
  }

  configDirty(): boolean {
    return JSON.stringify(this.configuration) !== JSON.stringify(this.originalConfiguration);
  }

  saveChanges(): void {
    if(this.usersDirty())
      this.saveUserChanges();
    if(this.permissionsDirty())
      this.savePermissionsChanges();
  }

  abandonChanges(): void {
    this.configuration = JSON.parse(JSON.stringify(this.originalConfiguration));
  }

  usersDirty(): boolean {
    return JSON.stringify(this.configuration["users"]) !== JSON.stringify(this.originalConfiguration["users"]);
  }

  permissionsDirty(): boolean {
    return JSON.stringify(this.configuration["permissions"]) !== JSON.stringify(this.originalConfiguration["permissions"]);
  }

  saveUserChanges(): void {
    let idsToAdd = {};
    let idsToRemove = {};
    for(let idx in this.configuration["users"]) {
      idsToAdd[this.configuration["users"][idx].id] = true;
    }
    for(let idx in this.originalConfiguration["users"]) {
      idsToRemove[this.originalConfiguration["users"][idx].id] = true;
      delete idsToAdd[this.originalConfiguration["users"][idx].id];
    }
    for(let idx in this.configuration["users"]) {
      delete idsToRemove[this.configuration["users"][idx].id];
    }
    for(let id in idsToAdd) {
      this.api.addUserToGroup(this.group.group.tenantId, this.group.group.groupId, id).subscribe(
        users => {
          this.originalConfiguration["users"] = JSON.parse(JSON.stringify(users));
        }
      );
    }
    for(let id in idsToRemove) {
      this.api.removeUserFromGroup(this.group.group.tenantId, this.group.group.groupId, id).subscribe(
        users => {
          this.originalConfiguration["users"] = JSON.parse(JSON.stringify(users));
        }
      );
    }
  }

  savePermissionsChanges(): void {
    let permissionsToAdd = {};
    let permissionsToRemove = {};
    for(let idx in this.configuration["permissions"]) {
      permissionsToAdd[this.configuration["permissions"][idx]] = true;
    }
    for(let idx in this.originalConfiguration["permissions"]) {
      permissionsToRemove[this.originalConfiguration["permissions"][idx]] = true;
      delete permissionsToAdd[this.originalConfiguration["permissions"][idx]];
    }
    for(let idx in this.configuration["permissions"]) {
      delete permissionsToRemove[this.configuration["permissions"][idx]];
    }
    for(let permission in permissionsToAdd) {
      this.api.addPermissionToGroup(this.group.group.tenantId, this.group.group.groupId, permission).subscribe(
        permissions => {
          this.originalConfiguration["permissions"] = JSON.parse(JSON.stringify(permissions));
        }
      );
    }
    for(let permission in permissionsToRemove) {
      this.api.removePermissionFromGroup(this.group.group.tenantId, this.group.group.groupId, permission).subscribe(
        permissions => {
          this.originalConfiguration["permissions"] = JSON.parse(JSON.stringify(permissions));
        }
      );
    }
  }

  deleteGroup() {
    this.api.deleteGroup(this.group.group.tenantId, this.group.group.groupId).subscribe(s => {
      this.update.emit(true);
    });
  }
}
