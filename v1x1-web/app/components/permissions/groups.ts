import {Component} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1GroupMembership} from "../../model/v1x1_group_membership";
import {V1x1GlobalState} from "../../services/global_state";

@Component({
  selector: 'permissions-groups-page',
  template: `
    <mat-tab-group>
      <mat-tab *ngFor="let group of groups">
        <ng-template mat-tab-label>{{group.group.name}}</ng-template>
        <permissions-group-page [activeTenant]="globalState.activeTenant.get() | async" [group]="group" (update)="recalculateGroups()"></permissions-group-page>
      </mat-tab>
      <mat-tab>
        <ng-template mat-tab-label>+ New</ng-template>
        <div class="card-container">
          <form>
            <mat-card>
              <mat-card-header>
                <mat-card-title><h1>New Group</h1></mat-card-title>
                <mat-card-subtitle>This lets you create a new group of users.</mat-card-subtitle>
              </mat-card-header>
              <mat-card-content>
                <mat-form-field>
                  <input matInput name="groupname" type="text" [(ngModel)]="newGroup" class="form-control" placeholder="New group name">
                </mat-form-field>
              </mat-card-content>
              <mat-card-actions>
                <button mat-raised-button color="primary" (click)="createGroup();">Create</button>
              </mat-card-actions>
            </mat-card>
          </form>
        </div>
      </mat-tab>
    </mat-tab-group>
  `
})
export class PermissionsGroupsComponent {
  groups: V1x1GroupMembership[] = [];
  newGroup: string = "";

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api, private globalState: V1x1GlobalState) {
    this.recalculateGroups();
  }

  recalculateGroups() {
    this.globalState.activeTenant.get().subscribe(tenant => {
      this.cachedApi.getGroups(tenant.id)
        .subscribe(groups => this.groups = groups);
    });
  }

  createGroup() {
    let newGroupName = this.newGroup;
    this.newGroup = "";
    this.api.createGroup(this.globalState.activeTenant.getCurrent().id, newGroupName).subscribe(newGroup => {
      this.cachedApi.clearGroupsCache();
      this.recalculateGroups();
    });
  }
}
