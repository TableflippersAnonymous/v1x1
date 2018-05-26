import {Injectable} from "@angular/core";
import {V1x1Api} from "./api";
import {V1x1Module} from "../model/v1x1_module";
import {Observable} from "rxjs";
import {V1x1PermissionDefinition} from "../model/v1x1_permission_definition";
import {publishReplay, refCount} from "rxjs/operators";
import {V1x1GroupMembership} from "../model/v1x1_group_membership";
import {V1x1ChannelGroupPlatformGroup} from "../model/v1x1_channel_group_platform_group";

@Injectable()
export class V1x1ApiCache {
  private modules: Observable<V1x1Module[]>;
  private permissions: Observable<V1x1PermissionDefinition[]>;
  private groups: {[key: string]: Observable<V1x1GroupMembership[]>} = {};
  private platformGroups: {[key: string]: Observable<V1x1ChannelGroupPlatformGroup[]>} = {};

  constructor(private api: V1x1Api) {}

  preload() {
    this.getModules().subscribe();
    this.getPermissions().subscribe();
  }

  getModules(): Observable<V1x1Module[]> {
    if(!this.modules)
      this.modules = this.api.getModules().pipe(publishReplay(1), refCount());
    return this.modules;
  }

  getPermissions(): Observable<V1x1PermissionDefinition[]> {
    if(!this.permissions)
      this.permissions = this.api.getPermissionDefinitions().pipe(publishReplay(1), refCount());
    return this.permissions;
  }

  getGroups(tenantId: string): Observable<V1x1GroupMembership[]> {
    if(!this.groups[tenantId])
      this.groups[tenantId] = this.api.getTenantGroupWithMemberships(tenantId).pipe(publishReplay(1), refCount());
    return this.groups[tenantId];
  }

  clearGroupsCache(): void {
    this.groups = {};
  }

  getPlatformGroups(tenantId: string, platform: string, channelGroupId: string): Observable<V1x1ChannelGroupPlatformGroup[]> {
    let key = tenantId + '/' + platform + '/' + channelGroupId;
    if(!this.platformGroups[key])
      this.platformGroups[key] = this.api.getChannelGroupPlatformGroups(tenantId, platform, channelGroupId).pipe(publishReplay(1), refCount());
    return this.platformGroups[key];
  }
}
