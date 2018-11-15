import {Injectable} from "@angular/core";
import {ObservableVariable} from "./util/observable_variable";
import {WebApp} from "../model/state/webapp";
import {V1x1Api} from "./api";
import {V1x1WebInfo} from "./web_info";
import {map, publishReplay, take} from "rxjs/operators";
import {Module} from "../model/state/module";
import {V1x1GlobalUser} from "../model/api/v1x1_global_user";
import {GlobalUser} from "../model/state/global_user";
import {User} from "../model/state/user";
import {V1x1User} from "../model/api/v1x1_user";
import {Tenant} from "../model/state/tenant";
import {ChannelGroup} from "../model/state/channel_group";
import {Observable} from "rxjs/internal/Observable";
import {Configuration} from "../model/state/configuration";
import {TenantGroup} from "../model/state/tenant_group";
import {Channel} from "../model/state/channel";
import {ApiWebApp} from "../model/api/api_web_app";
import {ApiSyncTenant} from "../model/api/api_sync_tenant";
import {ApiSyncChannelGroup} from "../model/api/api_sync_channel_group";
import {ApiChannelGroupPlatformGroup} from "../model/api/api_channel_group_platform_group";
import {ApiSyncChannel} from "../model/api/api_sync_channel";
import {ApiTenantGroup} from "../model/api/api_tenant_group";
import {ApiConfiguration} from "../model/api/api_configuration";
import {ChannelGroupPlatformGroup} from "../model/state/channel_group_platform_group";

@Injectable()
export class V1x1GlobalState {
  loggedIn: ObservableVariable<boolean> = new ObservableVariable<boolean>(false);
  authorization: ObservableVariable<string> = new ObservableVariable<string>(null);

  webapp: WebApp = new WebApp();

  constructor(private api: V1x1Api, private webInfo: V1x1WebInfo) {}

  init() {
    this.webInfo.getWebConfig().pipe(take(1)).subscribe(this.webapp.configuration);
    this.api.getModules().pipe(
      map(
        (modules: Module[]) => {
          let moduleMap: Map<string, Module> = new Map<string, Module>();
          modules.forEach((value: Module) => { moduleMap.set(value.name, value); });
          return moduleMap;
        }
      ),
      take(1)
    ).subscribe(this.webapp.modules);
  }

  login() {
    let apiWebApp: Observable<ApiWebApp> = this.api.getSync().pipe(
      take(1),
      publishReplay(1)
    );
    apiWebApp.pipe(map(r => this.convertToGlobalUser(r.current_user))).subscribe(this.webapp.currentUser);
    apiWebApp.pipe(map(r => this.convertToTenants(r.tenants))).subscribe(this.webapp.tenants);
  }

  private convertToGlobalUser(v1x1GlobalUser: V1x1GlobalUser): GlobalUser {
    let users: Map<string, User> = new Map<string, User>();
    let globalUser: GlobalUser = new GlobalUser(v1x1GlobalUser.id, users);
    v1x1GlobalUser.users.forEach((user: V1x1User) => { users.set(user.platform + ":" + user.userId, new User(globalUser, user.platform, user.userId, user.displayName)); });
    return globalUser;
  }

  private convertToTenants(apiTenants: {[tenantId: string]: ApiSyncTenant}): Map<string, Tenant> {
    let tenants: Map<string, Tenant> = new Map<string, Tenant>();
    for(let tenantId in apiTenants) {
      tenants.set(tenantId, this.convertToTenant(apiTenants[tenantId]));
    }
    return tenants;
  }

  private convertToTenant(apiTenant: ApiSyncTenant): Tenant {
    let channelGroups: Map<string, ChannelGroup> = new Map<string, ChannelGroup>();
    let moduleConfigurations: Map<string, Configuration> = new Map<string, Configuration>();
    let tenantGroups: Map<string, TenantGroup> = new Map<string, TenantGroup>();
    let tenant = new Tenant(apiTenant.id, apiTenant.display_name, channelGroups, moduleConfigurations, tenantGroups, []);
    for(let groupId in apiTenant.groups) {
      tenantGroups.set(groupId, this.convertToTenantGroup(tenant, apiTenant.groups[groupId]));
    }
    for(let moduleName in apiTenant.module_configuration) {
      moduleConfigurations.set(moduleName, this.convertToConfiguration(apiTenant.module_configuration[moduleName]));
    }
    for(let channelGroupId in apiTenant.channel_groups) {
      channelGroups.set(channelGroupId, this.convertToChannelGroup(tenant, apiTenant.channel_groups[channelGroupId]));
    }
    return tenant;
  }

  private convertToChannelGroup(tenant: Tenant, apiChannelGroup: ApiSyncChannelGroup): ChannelGroup {
    let channels: Map<string, Channel> = new Map<string, Channel>();
    let moduleConfigurations: Map<string, Configuration> = new Map<string, Configuration>();
    let groupMappings: Map<string, TenantGroup> = new Map<string, TenantGroup>();
    let channelGroup = new ChannelGroup(tenant, apiChannelGroup.platform, apiChannelGroup.id, apiChannelGroup.display_name,
      channels, moduleConfigurations, groupMappings,
      apiChannelGroup.platform_groups.map((channelGroupPlatformGroup: ApiChannelGroupPlatformGroup) =>
        this.convertToChannelGroupPlatformGroup(channelGroupPlatformGroup)),
      apiChannelGroup.joined);
    for(let channelId in apiChannelGroup.channels) {
      channels.set(channelId, this.convertToChannel(channelGroup, apiChannelGroup.channels[channelId]));
    }
    for(let moduleName in apiChannelGroup.module_configuration) {
      moduleConfigurations.set(moduleName, this.convertToConfiguration(apiChannelGroup.module_configuration[moduleName]));
    }
    for(let platformGroupName in apiChannelGroup.group_mappings) {
      groupMappings.set(platformGroupName, tenant.groups.get(apiChannelGroup.group_mappings[platformGroupName]));
    }
    return channelGroup;
  }

  private convertToChannel(channelGroup: ChannelGroup, apiChannel: ApiSyncChannel): Channel {
    let moduleConfigurations: Map<string, Configuration> = new Map<string, Configuration>();
    let channel = new Channel(channelGroup, apiChannel.id, apiChannel.display_name, moduleConfigurations);
    for(let moduleName in apiChannel.module_configuration) {
      moduleConfigurations.set(moduleName, this.convertToConfiguration(apiChannel.module_configuration[moduleName]));
    }
    return channel;
  }

  private convertToTenantGroup(tenant: Tenant, apiTenantGroup: ApiTenantGroup): TenantGroup {
    return new TenantGroup(tenant, apiTenantGroup.group_id, apiTenantGroup.name, apiTenantGroup.permissions,
      apiTenantGroup.users.map((v1x1GlobalUser: V1x1GlobalUser) => this.convertToGlobalUser(v1x1GlobalUser)));
  }

  private convertToConfiguration(apiConfiguration: ApiConfiguration): Configuration {
    return new Configuration(apiConfiguration.enabled, JSON.parse(apiConfiguration.config_json));
  }

  private convertToChannelGroupPlatformGroup(apiChannelGroupPlatformGroup: ApiChannelGroupPlatformGroup): ChannelGroupPlatformGroup {
    return new ChannelGroupPlatformGroup(apiChannelGroupPlatformGroup.name, apiChannelGroupPlatformGroup.display_name);
  }
}
