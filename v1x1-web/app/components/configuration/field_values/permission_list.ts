import {Component, Input, OnDestroy, OnInit} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
import {V1x1Api} from "../../../services/api";
import {V1x1ApiCache} from "../../../services/api_cache";
import {V1x1PermissionDefinition} from "../../../model/v1x1_permission_definition";
import {Subscription} from "rxjs/Subscription";
import {V1x1PermissionDefinitionEntry} from "../../../model/v1x1_permission_definition_entry";

@Component({
  selector: 'configuration-field-value-permission-list',
  template: `
    <div *ngFor="let elem of configuration; let i = index" style="padding-left: 10px; margin-bottom: 1rem;" class="config-group" [class.config-group-dirty]="configIdxDirty(i)">
      <div class="input-group">
        <span><permission-formatter [permissionNode]="configuration[i]"></permission-formatter></span><br>
        <span><button mat-raised-button color="accent" style="margin-left: 1rem;" (click)="delIdx(i);">&times;</button></span>
      </div>
    </div>
    <div>
      <div class="input-group">
        <mat-form-field>
          <mat-select placeholder="Permission" [(value)]="newPermission">
            <span *ngFor="let permissionDefinition of permissions">
              <mat-option *ngFor="let permissionDefinitionEntry of filterPermissions(permissionDefinition.entries)" [value]="permissionDefinitionEntry.node">
                {{permissionDefinitionEntry.displayName}}
              </mat-option>
            </span>
          </mat-select>
        </mat-form-field>
        <span class="input-group-append">
          <button mat-button color="accent" (click)="addList()">+</button>
        </span>
      </div>
    </div>
`
})
export class ConfigurationFieldValuePermissionListComponent extends ConfigurableComponent implements OnInit, OnDestroy {
  @Input() public field: V1x1ConfigurationDefinitionField;
  newPermission: string;
  permissions: V1x1PermissionDefinition[];

  private subscription: Subscription = null;

  constructor(private api: V1x1Api, private apiCache: V1x1ApiCache) {
    super();
  }

  public addList() {
    if(!(this.configuration instanceof Array))
      this.configuration = [];
    this.configuration.push(this.newPermission);
    this.configChanged();
  }

  public delIdx(index: number) {
    this.configuration.splice(index, 1);
    this.configChanged();
  }

  filterPermissions(entries: V1x1PermissionDefinitionEntry[]): V1x1PermissionDefinitionEntry[] {
    return entries.filter(entry => this.configuration.reduce((m, e) => m && e !== entry.node, true));
  }

  configDirty(): boolean {
    let idsToAdd = {};
    let idsToRemove = {};
    for(let idx in this.configuration) {
      idsToAdd[this.configuration[idx].id] = true;
    }
    for(let idx in this.originalConfiguration) {
      idsToRemove[this.originalConfiguration[idx].id] = true;
      delete idsToAdd[this.originalConfiguration[idx].id];
    }
    for(let idx in this.configuration) {
      delete idsToRemove[this.configuration[idx].id];
    }
    for(let id in idsToAdd) {
      return true;
    }
    for(let id in idsToRemove) {
      return true;
    }
    return false;
  }

  configIdxDirty(idx: number): boolean {
    for(let idx2 in this.originalConfiguration)
      if(this.originalConfiguration[idx2].id === this.configuration[idx].id)
        return false;
    return true;
  }

  ngOnInit(): void {
    this.subscription = this.apiCache.getPermissions().subscribe(
      permissions => {
        this.permissions = permissions;
      }
    );
  }

  ngOnDestroy(): void {
    if(this.subscription != null)
      this.subscription.unsubscribe();
  }
}
