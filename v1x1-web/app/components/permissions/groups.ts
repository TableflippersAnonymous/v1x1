import {Component, Input} from "@angular/core";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1GroupMembership} from "../../model/v1x1_group_membership";
@Component({
  selector: 'permissions-groups-page',
  template: `
    <ngb-tabset class="tabs-left">
      <ngb-tab *ngFor="let group of groups" [title]="group.group.name">
        <template ngbTabContent>
          <permissions-group-page [activeTenant]="activeTenant" [group]="group"></permissions-group-page>
        </template>
      </ngb-tab>
      <ngb-tab [title]="'+ New'" *ngIf="false">
        <template ngbTabContent>
          Not yet implemented
        </template>
      </ngb-tab>
    </ngb-tabset>
  `
})
export class PermissionsGroupsComponent {
  public activeTenantValue: V1x1Tenant = null;
  groups: V1x1GroupMembership[] = [];

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api) {
    this.recalculateGroups();
  }

  @Input()
  set activeTenant(activeTenant: V1x1Tenant) {
    this.activeTenantValue = activeTenant;
    this.recalculateGroups();
  }

  recalculateGroups() {
    if(this.activeTenantValue === null)
      return;
    this.api.getTenantGroupWithMemberships(this.activeTenantValue.id)
      .subscribe(groups => this.groups = groups);
  }
}
