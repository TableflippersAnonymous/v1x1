import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {Subscription} from "rxjs/Subscription";
import {V1x1PermissionDefinition} from "../../model/v1x1_permission_definition";
import {V1x1PermissionDefinitionEntry} from "../../model/v1x1_permission_definition_entry";

@Component({
  selector: 'permission-formatter',
  template: `
    <div *ngIf="permission === undefined"><p>Unknown permission: {{permissionNode}}</p></div>
    <div *ngIf="permission !== undefined">
      <p>{{permission.displayName}}</p>
      <small>{{permission.description}}</small>
    </div>
  `
})
export class PermissionFormatterComponent implements OnInit, OnDestroy, OnChanges {
  @Input() permissionNode: string;
  permission: V1x1PermissionDefinitionEntry;

  private subscription: Subscription = null;

  constructor(private api: V1x1Api, private apiCache: V1x1ApiCache) {}

  ngOnInit(): void {
    this.reload();
  }

  ngOnDestroy(): void {
    if(this.subscription != null)
      this.subscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.reload();
  }

  reload(): void {
    if(this.subscription != null)
      this.subscription.unsubscribe();
    this.subscription = this.apiCache.getPermissions().subscribe(
      (permissions: V1x1PermissionDefinition[]) => {
        this.permission = permissions.map(permission => permission.entries.find(entry => entry.node === this.permissionNode))
          .find(permission => permission !== undefined);
      }
    );
  }
}
