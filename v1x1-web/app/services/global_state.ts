import {Injectable} from "@angular/core";
import {V1x1Tenant} from "../model/api/v1x1_tenant";
import {ObservableVariable} from "./util/observable_variable";
import {WebApp} from "../model/state/webapp";
import {V1x1Api} from "./api";
import {V1x1WebInfo} from "./web_info";
import {forkJoin} from "rxjs";
import {map, mergeAll, publishReplay, refCount} from "rxjs/operators";
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

@Injectable()
export class V1x1GlobalState {
  activeTenant: ObservableVariable<V1x1Tenant> = new ObservableVariable<V1x1Tenant>(undefined);
  loggedIn: ObservableVariable<boolean> = new ObservableVariable<boolean>(false);
  authorization: ObservableVariable<string> = new ObservableVariable<string>(null);

  webapp: WebApp = new WebApp();

  constructor(private api: V1x1Api, private webInfo: V1x1WebInfo) {}

  init() {
    this.webapp.configuration = this.webInfo.getWebConfig().pipe(publishReplay(1), refCount());
    this.webapp.modules = this.api.getModules().pipe(
      map(
        (modules: Module[]) => {
          let moduleMap: Map<string, Module> = new Map<string, Module>();
          modules.forEach((value: Module) => { moduleMap.set(value.name, value); });
          return moduleMap;
        }
      ),
      publishReplay(1),
      refCount()
    );
  }

  login() {
    this.webapp.currentUser = this.api.getSelf().pipe(
      map((v1x1GlobalUser: V1x1GlobalUser) => this.convertToGlobalUser(v1x1GlobalUser)),
      publishReplay(1),
      refCount()
    );
    this.webapp.tenants = forkJoin(
      this.api.getTenants(),
      this.webapp.modules
    ).pipe(
      map(([v1x1Tenants, modules]: [V1x1Tenant[], Map<string, Module>]) => {
        let observables: Observable<{v1x1Tenant: V1x1Tenant, modules: Map<string, V1x1ConfigurationSet>}>[] = [];
        v1x1Tenants.forEach((v1x1Tenant: V1x1Tenant) => {
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
          observables.push(forkJoin(
            this.api.getTenantGroupWithMemberships(v1x1Tenant.id),
            forkJoin(moduleObservables),
            forkJoin(platformMappingsObservables)
          ).pipe(
            map(([groups, entries, platformMappings]: [V1x1GroupMembership[], {module: Module, configurationSet: V1x1ConfigurationSet}[], V1x1ChannelGroupPlatformMappingWrapper[]]) => {
              let modules: Map<string, V1x1ConfigurationSet> = new Map<string, V1x1ConfigurationSet>();
              entries.forEach((entry: {module: Module, configurationSet: V1x1ConfigurationSet}) => {
                modules.set(entry.module.name, entry.configurationSet);
              });
              return {v1x1Tenant: v1x1Tenant, groups: groups, modules: modules, platformMappings: platformMappings};
            })
          ));
        });
        return forkJoin(observables).pipe(
          map((entries: {v1x1Tenant: V1x1Tenant, groups: V1x1GroupMembership[], modules: Map<string, V1x1ConfigurationSet>, platformMappings: V1x1ChannelGroupPlatformMappingWrapper[]}[]) => {
            return entries.map((entry: {v1x1Tenant: V1x1Tenant, groups: V1x1GroupMembership[], modules: Map<string, V1x1ConfigurationSet>, platformMappings: V1x1ChannelGroupPlatformMappingWrapper[]}) => {
              let channelGroups: Map<string, ChannelGroup> = new Map<string, ChannelGroup>();
              let tenantModuleConfiguration: Map<string, Configuration> = new Map<string, Configuration>();
              entry.modules.forEach((configSet: V1x1ConfigurationSet, moduleName: string) => {
                tenantModuleConfiguration.set(moduleName, configSet.tenant);
              });
              let groups: Map<string, TenantGroup> = new Map<string, TenantGroup>();
              let tenant: Tenant = new Tenant(entry.v1x1Tenant.id, entry.v1x1Tenant.displayName, channelGroups, tenantModuleConfiguration, groups, []);
              entry.groups.forEach((groupMembership: V1x1GroupMembership) => {
                groups.set(groupMembership.group.groupId, new TenantGroup(
                  tenant, groupMembership.group.groupId, groupMembership.group.name, groupMembership.group.permissions,
                  groupMembership.members.map((v1x1GlobalUser: V1x1GlobalUser) => this.convertToGlobalUser(v1x1GlobalUser))
                ));
              });
              entry.v1x1Tenant.channelGroups.forEach((v1x1ChannelGroup: V1x1ChannelGroup) => {
                let channelGroupModuleConfiguration: Map<string, Configuration> = new Map<string, Configuration>();
                entry.modules.forEach((configSet: V1x1ConfigurationSet, moduleName: string) => {
                  channelGroupModuleConfiguration.set(moduleName, configSet.channelGroups.find((v1x1ChannelGroupConfigurationWrapper: V1x1ChannelGroupConfigurationWrapper) =>
                    v1x1ChannelGroupConfigurationWrapper.channelGroup.platform == v1x1ChannelGroup.platform &&
                      v1x1ChannelGroupConfigurationWrapper.channelGroup.id == v1x1ChannelGroup.id).config.channelGroup);
                });
                let v1x1ChannelGroupPlatformMappingWrapper: V1x1ChannelGroupPlatformMappingWrapper = entry.platformMappings.find(
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
                  false /* TODO: Fix this */
                );
                v1x1ChannelGroup.channels.forEach((v1x1Channel: V1x1Channel) => {
                  let channelModuleConfiguration: Map<string, Configuration> = new Map<string, Configuration>();
                  entry.modules.forEach((configSet: V1x1ConfigurationSet, moduleName: string) => {
                    channelModuleConfiguration.set(moduleName, configSet.channelGroups.find((v1x1ChannelGroupConfigurationWrapper: V1x1ChannelGroupConfigurationWrapper) =>
                      v1x1ChannelGroupConfigurationWrapper.channelGroup.platform == v1x1ChannelGroup.platform &&
                        v1x1ChannelGroupConfigurationWrapper.channelGroup.id == v1x1ChannelGroup.id
                    ).config.channels.find((v1x1ChannelConfigurationWrapper: V1x1ChannelConfigurationWrapper) =>
                      v1x1ChannelConfigurationWrapper.channel.id == v1x1Channel.id
                    ).config);
                  });
                  channels.set(v1x1Channel.id, new Channel(
                    channelGroup, v1x1Channel.id, v1x1Channel.displayName, channelModuleConfiguration
                  ));
                });
                channelGroups.set(v1x1ChannelGroup.platform + ":" + v1x1ChannelGroup.platform, channelGroup);
              });
              return tenant;
            });
          })
        );
      }),
      mergeAll(),
      map((tenants: Tenant[]) => {
        let map: Map<string, Tenant> = new Map<string, Tenant>();
        tenants.forEach((tenant: Tenant) => {
          map.set(tenant.id, tenant);
        });
        return map;
      }),
      publishReplay(1),
      refCount()
    );
  }

  convertToGlobalUser(v1x1GlobalUser: V1x1GlobalUser) {
    let users: Map<string, User> = new Map<string, User>();
    let globalUser: GlobalUser = new GlobalUser(v1x1GlobalUser.id, users);
    v1x1GlobalUser.users.forEach((user: V1x1User) => { users.set(user.platform + ":" + user.userId, new User(globalUser, user.platform, user.userId, user.displayName)); });
    return globalUser;
  }
}
