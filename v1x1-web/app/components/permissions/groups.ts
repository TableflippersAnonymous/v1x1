import {Component} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1GroupMembership} from "../../model/v1x1_group_membership";
import {V1x1GlobalState} from "../../services/global_state";

@Component({
  selector: 'permissions-groups-page',
  template: `
    <ngb-tabset class="tabs-left">
      <ngb-tab *ngFor="let group of groups" [title]="group.group.name">
        <ng-template ngbTabContent>
          <permissions-group-page [activeTenant]="globalState.activeTenant.get() | async" [group]="group" (update)="recalculateGroups()"></permissions-group-page>
        </ng-template>
      </ngb-tab>
      <ngb-tab [title]="'+ New'">
        <ng-template ngbTabContent>
          <div class="jumbotron" style="margin: 1rem;">
            <h1 class="display-3">New Group</h1>
            <p class="lead">This lets you create a new group of users.</p>
            <hr class="my-4">
            <p>Additional help can be found in the help pages.</p>
          </div>
          <form>
            <div class="form-group">
              <label>Group Name</label>
              <div class="input-group">
                <input name="groupname" type="text" [(ngModel)]="newGroup" class="form-control" placeholder="New group name">
              </div>
            </div>
            <button class="btn btn-primary" (click)="createGroup();">Create</button>
          </form>
        </ng-template>
      </ngb-tab>
    </ngb-tabset>
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
      this.api.getTenantGroupWithMemberships(tenant.id)
        .subscribe(groups => this.groups = groups);
    });
  }

  createGroup() {
    let newGroupName = this.newGroup;
    this.newGroup = "";
    this.api.createGroup(this.globalState.activeTenant.getCurrent().id, newGroupName).subscribe(newGroup => {
      this.recalculateGroups();
    });
  }
}
