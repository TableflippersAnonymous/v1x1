import {Injectable} from "@angular/core";
import {V1x1Tenant} from "../model/api/v1x1_tenant";
import {ObservableVariable} from "./util/observable_variable";
import {WebApp} from "../model/state/webapp";
import {V1x1Api} from "./api";
import {V1x1WebInfo} from "./web_info";
import {forkJoin} from "rxjs";
import {map, mergeAll, take} from "rxjs/operators";
import {Module} from "../model/state/module";
import {V1x1GlobalUser} from "../model/api/v1x1_global_user";
import {GlobalUser} from "../model/state/global_user";
import {User} from "../model/state/user";
import {V1x1User} from "../model/api/v1x1_user";
import {Tenant} from "../model/state/tenant";
import {ChannelGroup} from "../model/state/channel_group";
import {V1x1ConfigurationSet} from "../model/api/v1x1_configuration_set";
import {Observable} from "rxjs/internal/Observable";
import {V1x1ChannelGroup} from "../model/api/v1x1_channel_group";
import {Configuration} from "../model/state/configuration";
import {V1x1GroupMembership} from "../model/api/v1x1_group_membership";
import {TenantGroup} from "../model/state/tenant_group";
import {Channel} from "../model/state/channel";
import {V1x1Channel} from "../model/api/v1x1_channel";
import {V1x1ChannelGroupConfigurationWrapper} from "../model/api/v1x1_channel_group_configuration_wrapper";
import {V1x1ChannelConfigurationWrapper} from "../model/api/v1x1_channel_configuration_wrapper";
import {V1x1ChannelGroupPlatformMappingWrapper} from "../model/api/v1x1_channel_group_platform_mapping_wrapper";
import {V1x1ChannelGroupPlatformMapping} from "../model/api/v1x1_channel_group_platform_mapping";
import {V1x1ChannelGroupPlatformGroup} from "../model/api/v1x1_channel_group_platform_group";

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
    this.api.getSelf().pipe(
      map((v1x1GlobalUser: V1x1GlobalUser) => this.convertToGlobalUser(v1x1GlobalUser)),
      take(1)
    ).subscribe(this.webapp.currentUser);
    forkJoin(
      this.api.getTenants(),
      this.webapp.modules
    ).pipe(
      map(([v1x1Tenants, modules]: [V1x1Tenant[], Map<string, Module>]) => {
        return forkJoin(v1x1Tenants.map((v1x1Tenant: V1x1Tenant) => this.getTenant(v1x1Tenant, modules)));
      }),
      mergeAll(),
      map((tenants: Tenant[]) => {
        let map: Map<string, Tenant> = new Map<string, Tenant>();
        tenants.forEach((tenant: Tenant) => {
          map.set(tenant.id, tenant);
        });
        return map;
      }),
      take(1)
    ).subscribe(this.webapp.tenants);
  }

  convertToGlobalUser(v1x1GlobalUser: V1x1GlobalUser) {
    let users: Map<string, User> = new Map<string, User>();
    let globalUser: GlobalUser = new GlobalUser(v1x1GlobalUser.id, users);
    v1x1GlobalUser.users.forEach((user: V1x1User) => { users.set(user.platform + ":" + user.userId, new User(globalUser, user.platform, user.userId, user.displayName)); });
    return globalUser;
  }

  getTenant(v1x1Tenant: V1x1Tenant, modules: Map<string, Module>): Observable<Tenant> {
    let moduleObservables: Observable<{module: Module, configurationSet: V1x1ConfigurationSet}>[] = [];
    modules.forEach((module: Module) => {
      moduleObservables.push(this.api.getTenantConfigurationSet(v1x1Tenant.id, module.name).pipe(
        map((v1x1ConfigurationSet: V1x1ConfigurationSet) => {
          return {module: module, configurationSet: v1x1ConfigurationSet};
        })
      ));
    });
    let platformMappingsObservables: Observable<V1x1ChannelGroupPlatformMappingWrapper>[] =
      v1x1Tenant.channelGroups.map((v1x1ChannelGroup: V1x1ChannelGroup) => this.api.getChannelGroupPlatformMappingWrapper(v1x1Tenant.id, v1x1ChannelGroup));
    forkJoin(
      this.api.getTenantGroupWithMemberships(v1x1Tenant.id),
      forkJoin(moduleObservables),
      forkJoin(platformMappingsObservables)
    ).pipe(
      map(([groupMemberships, entries, platformMappings]: [V1x1GroupMembership[], {module: Module, configurationSet: V1x1ConfigurationSet}[], V1x1ChannelGroupPlatformMappingWrapper[]]) => {
        return this.buildTenant(entries, v1x1Tenant, groupMemberships, platformMappings);
      })
    );
  }

  private buildTenant(entries: { module: Module; configurationSet: V1x1ConfigurationSet }[], v1x1Tenant: V1x1Tenant,
                      groupMemberships: V1x1GroupMembership[], platformMappings: V1x1ChannelGroupPlatformMappingWrapper[]) {
    let modules: Map<string, V1x1ConfigurationSet> = new Map<string, V1x1ConfigurationSet>();
    entries.forEach((entry: { module: Module, configurationSet: V1x1ConfigurationSet }) => {
      modules.set(entry.module.name, entry.configurationSet);
    });
    let channelGroups: Map<string, ChannelGroup> = new Map<string, ChannelGroup>();
    let tenantModuleConfiguration: Map<string, Configuration> = new Map<string, Configuration>();
    modules.forEach((configSet: V1x1ConfigurationSet, moduleName: string) => {
      tenantModuleConfiguration.set(moduleName, configSet.tenant);
    });
    let groups: Map<string, TenantGroup> = new Map<string, TenantGroup>();
    let tenant: Tenant = new Tenant(v1x1Tenant.id, v1x1Tenant.displayName, channelGroups, tenantModuleConfiguration, groups, []);
    groupMemberships.forEach((groupMembership: V1x1GroupMembership) => {
      groups.set(groupMembership.group.groupId, new TenantGroup(
        tenant, groupMembership.group.groupId, groupMembership.group.name, groupMembership.group.permissions,
        groupMembership.members.map((v1x1GlobalUser: V1x1GlobalUser) => this.convertToGlobalUser(v1x1GlobalUser))
      ));
    });
    v1x1Tenant.channelGroups.forEach((v1x1ChannelGroup: V1x1ChannelGroup) => {
      channelGroups.set(v1x1ChannelGroup.platform + ":" + v1x1ChannelGroup.platform,
        this.buildChannelGroup(modules, v1x1ChannelGroup, platformMappings, groups, tenant, platformGroups));
    });
    return tenant;
  }

  private buildChannelGroup(modules: Map<string, V1x1ConfigurationSet>, v1x1ChannelGroup: V1x1ChannelGroup,
                            platformMappings: V1x1ChannelGroupPlatformMappingWrapper[], groups: Map<string, TenantGroup>,
                            tenant: Tenant, platformGroups: V1x1ChannelGroupPlatformGroup[]) {
    let channelGroupModuleConfiguration: Map<string, Configuration> = new Map<string, Configuration>();
    modules.forEach((configSet: V1x1ConfigurationSet, moduleName: string) => {
      channelGroupModuleConfiguration.set(moduleName, configSet.channelGroups.find((v1x1ChannelGroupConfigurationWrapper: V1x1ChannelGroupConfigurationWrapper) =>
        v1x1ChannelGroupConfigurationWrapper.channelGroup.platform == v1x1ChannelGroup.platform &&
        v1x1ChannelGroupConfigurationWrapper.channelGroup.id == v1x1ChannelGroup.id).config.channelGroup);
    });
    let v1x1ChannelGroupPlatformMappingWrapper: V1x1ChannelGroupPlatformMappingWrapper = platformMappings.find(
      (v1x1ChannelGroupPlatformMappingWrapper: V1x1ChannelGroupPlatformMappingWrapper) =>
        v1x1ChannelGroupPlatformMappingWrapper.channelGroup.platform == v1x1ChannelGroup.platform &&
        v1x1ChannelGroupPlatformMappingWrapper.channelGroup.id == v1x1ChannelGroup.id);
    let groupMappings: Map<string, TenantGroup> = new Map<string, TenantGroup>();
    v1x1ChannelGroupPlatformMappingWrapper.mappings.forEach((v1x1ChannelGroupPlatformMapping: V1x1ChannelGroupPlatformMapping) => {
      groupMappings.set(v1x1ChannelGroupPlatformMapping.platformGroup, groups.get(v1x1ChannelGroupPlatformMapping.groupId));
    });
    let channels: Map<string, Channel> = new Map<string, Channel>();
    let channelGroup: ChannelGroup = new ChannelGroup(
      tenant,
      v1x1ChannelGroup.platform,
      v1x1ChannelGroup.id,
      v1x1ChannelGroup.displayName,
      channels,
      channelGroupModuleConfiguration,
      groupMappings,
      platformGroups,
      false /* TODO: Fix this */
    );
    v1x1ChannelGroup.channels.forEach((v1x1Channel: V1x1Channel) => {
      channels.set(v1x1Channel.id, this.buildChannel(modules, v1x1ChannelGroup, v1x1Channel, channelGroup));
    });
    return channelGroup;
  }

  private buildChannel(modules: Map<string, V1x1ConfigurationSet>, v1x1ChannelGroup: V1x1ChannelGroup, v1x1Channel: V1x1Channel, channelGroup: ChannelGroup) {
    let channelModuleConfiguration: Map<string, Configuration> = new Map<string, Configuration>();
    modules.forEach((configSet: V1x1ConfigurationSet, moduleName: string) => {
      channelModuleConfiguration.set(moduleName, configSet.channelGroups.find((v1x1ChannelGroupConfigurationWrapper: V1x1ChannelGroupConfigurationWrapper) =>
        v1x1ChannelGroupConfigurationWrapper.channelGroup.platform == v1x1ChannelGroup.platform &&
        v1x1ChannelGroupConfigurationWrapper.channelGroup.id == v1x1ChannelGroup.id
      ).config.channels.find((v1x1ChannelConfigurationWrapper: V1x1ChannelConfigurationWrapper) =>
        v1x1ChannelConfigurationWrapper.channel.id == v1x1Channel.id
      ).config);
    });
    let channel = new Channel(
      channelGroup, v1x1Channel.id, v1x1Channel.displayName, channelModuleConfiguration
    );
    return channel;
  }
}
