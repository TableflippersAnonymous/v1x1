import {V1x1Module} from "../model/v1x1_module";
import {Injectable} from "@angular/core";
import {V1x1ConfigurationDefinitionSet} from "../model/v1x1_configuration_definition_set";
import {V1x1ConfigurationDefinition} from "../model/v1x1_configuration_definition";
import {forkJoin, Observable, of, zip} from "rxjs";
import {V1x1List} from "../model/v1x1_list";
import {JsonConvert} from "json2typescript";
import {V1x1State} from "../model/v1x1_state";
import {V1x1AuthToken} from "../model/v1x1_auth_token";
import {V1x1OauthCode} from "../model/v1x1_oauth_code";
import {V1x1GlobalUser} from "../model/v1x1_global_user";
import {V1x1User} from "../model/v1x1_user";
import {V1x1ApiString} from "../model/v1x1_api_string";
import {V1x1Tenant} from "../model/v1x1_tenant";
import {V1x1Configuration} from "../model/v1x1_configuration";
import {V1x1Group} from "../model/v1x1_group";
import {V1x1GroupMembership} from "../model/v1x1_group_membership";
import {V1x1DisplayNameRecord} from "../model/v1x1_display_name_record";
import {V1x1WebInfo} from "./web_info";
import {V1x1GlobalState} from "./global_state";
import {V1x1ConfigurationSet} from "../model/v1x1_configuration_set";
import {V1x1ChannelGroup} from "../model/v1x1_channel_group";
import {V1x1ChannelGroupConfiguration} from "../model/v1x1_channel_group_configuration";
import {V1x1ChannelGroupConfigurationWrapper} from "../model/v1x1_channel_group_configuration_wrapper";
import {V1x1Channel} from "../model/v1x1_channel";
import {V1x1ChannelConfigurationWrapper} from "../model/v1x1_channel_configuration_wrapper";
import {V1x1ChannelGroupPlatformMapping} from "../model/v1x1_channel_group_platform_mapping";
import {V1x1TenantPlatformMapping} from "../model/v1x1_tenant_platform_mapping";
import {V1x1ChannelGroupPlatformMappingWrapper} from "../model/v1x1_channel_group_platform_mapping_wrapper";
import {HttpClient} from "@angular/common/http";
import {V1x1PermissionDefinition} from "../model/v1x1_permission_definition";
import {catchError, map, mergeAll} from 'rxjs/operators';

@Injectable()
export class V1x1Api {
  constructor(private http: HttpClient, private webInfo: V1x1WebInfo, private globalState: V1x1GlobalState) {}

  getConfigurationDefinitionList(type: string): Observable<V1x1ConfigurationDefinition[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/platform/config-definitions/' + type).pipe(
        map((r) => JsonConvert.deserializeObject(r, V1x1List)),
        map((l: V1x1List<any>) => l.entries.map(x => JsonConvert.deserializeObject(x, V1x1ConfigurationDefinition))))
    ), mergeAll());
  }

  getModuleList(global: V1x1ConfigurationDefinition[], user: V1x1ConfigurationDefinition[]): string[] {
    let moduleNames: string[] = global.concat(user).map((configDefinition: V1x1ConfigurationDefinition) => configDefinition.name);
    return [].concat.apply([], moduleNames).filter((v, idx, self) => self.indexOf(v) === idx);
  }

  getConfigurationDefinitions(): Observable<V1x1ConfigurationDefinitionSet[]> {
    return zip(
      this.getConfigurationDefinitionList("global"),
      this.getConfigurationDefinitionList("user"))
    .pipe(map(
      ([global, user]) => this.getModuleList(global, user)
        .map((moduleName: string) => new V1x1ConfigurationDefinitionSet(
          global.find((configDefinition: V1x1ConfigurationDefinition) => configDefinition.name == moduleName),
          user.find((configDefinition: V1x1ConfigurationDefinition) => configDefinition.name == moduleName)
        ))
    ));
  }

  getModule(configurationDefinitionSet: V1x1ConfigurationDefinitionSet): V1x1Module {
    return new V1x1Module((configurationDefinitionSet.global || configurationDefinitionSet.user).name, configurationDefinitionSet);
  }

  getModulesFromConfigurationDefinitions(configurationDefinitionSets: V1x1ConfigurationDefinitionSet[]): V1x1Module[] {
    return configurationDefinitionSets.map(
      (configDefinitionSet: V1x1ConfigurationDefinitionSet) =>
        this.getModule(configDefinitionSet)
    );
  }

  getModules(): Observable<V1x1Module[]> {
    return this.getConfigurationDefinitions().pipe(map(
      (configDefinitions: V1x1ConfigurationDefinitionSet[]) =>
        this.getModulesFromConfigurationDefinitions(configDefinitions)
    ));
  }

  getState(): Observable<V1x1State> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/meta/state').pipe(map((r) => JsonConvert.deserializeObject(r, V1x1State)))
    ), mergeAll());
  }

  loginTwitch(twitchOauthCode: V1x1OauthCode): Observable<V1x1AuthToken> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.post(
        wc.apiBase + '/meta/login/twitch',
        JsonConvert.serializeObject({
          'oauth_code': twitchOauthCode.oauthCode,
          'oauth_state': twitchOauthCode.oauthState
        }),
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      ).pipe(map((r) => JsonConvert.deserializeObject(r, V1x1AuthToken)))
    ), mergeAll());
  }

  loginDiscord(oauthCode: V1x1OauthCode): Observable<V1x1AuthToken> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.post(
        wc.apiBase + '/meta/login/discord',
        JsonConvert.serializeObject({
          'oauth_code': oauthCode.oauthCode,
          'oauth_state': oauthCode.oauthState
        }),
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      ).pipe(map((r) => JsonConvert.deserializeObject(r, V1x1AuthToken)))
    ), mergeAll());
  }

  getAuthorization(): {headers: {Authorization: string}} {
    return {
      headers: {
        Authorization: this.globalState.authorization.getCurrent()
      }
    };
  }

  getUserPlatforms(globalUserId: string): Observable<string[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + "/global-users/" + globalUserId + "/users", this.getAuthorization()).pipe(
        map((r) => JsonConvert.deserializeObject(r, V1x1List)),
        map((l: V1x1List<string>) => l.entries))
    ), mergeAll());
  }

  getUserIdsByPlatform(globalUserId: string, platform: string): Observable<string[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + "/global-users/" + globalUserId + "/users/" + platform, this.getAuthorization()).pipe(
        map((r) => JsonConvert.deserializeObject(r, V1x1List)),
        map((l: V1x1List<string>) => l.entries))
    ), mergeAll());
  }

  getUser(globalUserId: string, platform: string, userId: string): Observable<V1x1User> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + "/global-users/" + globalUserId + "/users/" + platform + "/" + userId, this.getAuthorization())
        .pipe(map((r) => JsonConvert.deserializeObject(r, V1x1User)))
    ), mergeAll());
  }

  getUsersByPlatform(globalUserId: string, platform: string): Observable<V1x1User[]> {
    return this.getUserIdsByPlatform(globalUserId, platform).pipe(
      map(userIds => userIds.map(userId => this.getUser(globalUserId, platform, userId))),
      map(observableUsers => forkJoin(observableUsers)),
      mergeAll());
  }

  getUsers(globalUserId: string): Observable<V1x1User[]> {
    return this.getUserPlatforms(globalUserId).pipe(
      map(platforms => platforms.map(platform => this.getUsersByPlatform(globalUserId, platform))),
      map(observableUsers => forkJoin(observableUsers)),
      mergeAll(),
      map(users => [].concat.apply([], users)));
  }

  getGlobalUser(globalUserId: string): Observable<V1x1GlobalUser> {
    return this.getUsers(globalUserId)
      .pipe(map(users => new V1x1GlobalUser(globalUserId, users)));
  }

  getGlobalUserByPlatformAndUsername(platform: string, username: string): Observable<V1x1GlobalUser> {
    return this.getDisplayNameRecordByUsername(platform, username).pipe(
      map(displayNameRecord => this.getGlobalUser(displayNameRecord.globalUserId)),
      mergeAll());
  }

  getDisplayNameRecordByUsername(platform: string, username: string): Observable<V1x1DisplayNameRecord> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + "/platform/display-name/" + encodeURIComponent(platform.toLowerCase()) + "/user/by-username/" + encodeURIComponent(username))
        .pipe(map(r => JsonConvert.deserializeObject(r, V1x1DisplayNameRecord)))
    ), mergeAll());
  }

  getSelf(): Observable<V1x1GlobalUser> {
    return this.getSelfId().pipe(
      map(globalUserId => this.getGlobalUser(globalUserId)),
      mergeAll());
  }

  getSelfId(): Observable<string> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/meta/self', this.getAuthorization()).pipe(
        map(r => JsonConvert.deserializeObject(r, V1x1ApiString)),
        map(r => r.value))
    ), mergeAll());
  }

  getTenantIds(): Observable<string[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/tenants', this.getAuthorization()).pipe(
        map((r) => JsonConvert.deserializeObject(r, V1x1List)),
        map((l: V1x1List<string>) => l.entries),
        map(tenantIds => tenantIds.filter(tenantId => tenantId !== '493073c3-8a6f-38fa-8e38-16af0b436482')))
    ), mergeAll());
  }

  getTenant(tenantId: string): Observable<V1x1Tenant> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId, this.getAuthorization())
        .pipe(map((r) => JsonConvert.deserializeObject(r, V1x1Tenant)))
    ), mergeAll());
  }

  getTenants(): Observable<V1x1Tenant[]> {
    return this.getTenantIds().pipe(
      map(tenantIds => tenantIds.map(tenantId => this.getTenant(tenantId))),
      map(observableTenants => forkJoin(observableTenants)),
      mergeAll(),
      map(tenants => tenants.filter(tenant => tenant !== null)));
  }

  getTenantConfigurationSet(tenantId: string, module: string): Observable<V1x1ConfigurationSet> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/config/' + module + '/all', this.getAuthorization())
        .pipe(map((r: any) => new V1x1ConfigurationSet(
          new V1x1Configuration(r.tenant_configuration.enabled, JSON.parse(r.tenant_configuration.config_json)),
          r.channel_group_configurations.map(
            channelGroupConfiguration => new V1x1ChannelGroupConfigurationWrapper(
              JsonConvert.deserializeObject(channelGroupConfiguration.channel_group, V1x1ChannelGroup),
              new V1x1ChannelGroupConfiguration(
                new V1x1Configuration(channelGroupConfiguration.channel_group_configuration.enabled, JSON.parse(channelGroupConfiguration.channel_group_configuration.config_json)),
                channelGroupConfiguration.channel_configurations.map(
                  channelConfiguration => new V1x1ChannelConfigurationWrapper(
                    JsonConvert.deserializeObject(channelConfiguration.channel, V1x1Channel),
                    new V1x1Configuration(channelConfiguration.channel_configuration.enabled, JSON.parse(channelConfiguration.channel_configuration.config_json))
                  )
                )
              )
            )
          )
        )))
    ), mergeAll());
  }

  putTenantConfiguration(tenantId: string, module: string, config: V1x1Configuration): Observable<V1x1Configuration> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.put(
        wc.apiBase + '/tenants/' + tenantId + '/config/' + module,
        JsonConvert.serializeObject({
          'config_json': JSON.stringify(config.configuration)
        }),
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          }
        })
          .pipe(map((r: {enabled: boolean, config_json: string}) => new V1x1Configuration(r.enabled, JSON.parse(r.config_json))))
    ), mergeAll());
  }

  putChannelGroupConfiguration(tenantId: string, module: string, platform: string, channelGroupId: string, config: V1x1Configuration): Observable<V1x1Configuration> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.put(
        wc.apiBase + '/tenants/' + tenantId + '/config/' + module + '/' + platform + '/' + channelGroupId,
        JsonConvert.serializeObject({
          'enabled': config.enabled,
          'config_json': JSON.stringify(config.configuration)
        }),
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          }
        })
        .pipe(map((r: {enabled: boolean, config_json: string}) => new V1x1Configuration(r.enabled, JSON.parse(r.config_json))))
    ), mergeAll());
  }

  putChannelConfiguration(tenantId: string, module: string, platform: string, channelGroupId: string, channelId: string, config: V1x1Configuration): Observable<V1x1Configuration> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.put(
        wc.apiBase + '/tenants/' + tenantId + '/config/' + module + '/' + platform + '/' + channelGroupId + '/' + channelId,
        JsonConvert.serializeObject({
          'enabled': config.enabled,
          'config_json': JSON.stringify(config.configuration)
        }),
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          }
        })
        .pipe(map((r: {enabled: boolean, config_json: string}) => new V1x1Configuration(r.enabled, JSON.parse(r.config_json))))
    ), mergeAll());
  }

  getTenantGroupWithMemberships(tenantId: string): Observable<V1x1GroupMembership[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/groups/all', this.getAuthorization()).pipe(
        map((r) => JsonConvert.deserializeObject(r, V1x1List)),
        map((l: V1x1List<any>) => l.entries),
        map((r: any) => r.map(
          (groupMembership: any) => new V1x1GroupMembership(
            JsonConvert.deserializeObject(groupMembership.group, V1x1Group),
            groupMembership.members.map(
              globalUser => new V1x1GlobalUser(
                globalUser.id,
                globalUser.users.map(
                  user => JsonConvert.deserializeObject(user, V1x1User)
                )
              )
            )
          )
        )))
    ), mergeAll());
  }

  getGroupUsers(tenantId: string, groupId: string): Observable<V1x1GlobalUser[]> {
    return this.getGroupUserIds(tenantId, groupId).pipe(
      map(userIds => userIds.map(userId => this.getGlobalUser(userId))),
      map(observableUsers => observableUsers.length === 0 ? of([]) : forkJoin(observableUsers)),
      mergeAll());
  }

  getGroupUserIds(tenantId: string, groupId: string): Observable<string[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/users', this.getAuthorization()).pipe(
        map((r) => JsonConvert.deserializeObject(r, V1x1List)),
        map((l: V1x1List<string>) => l.entries),
        catchError((err, caught) => of([])))
    ), mergeAll());
  }

  addUserToGroup(tenantId: string, groupId: string, userId: string): Observable<V1x1GlobalUser[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.post(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/users',
        JsonConvert.serializeObject({
          'value': userId
        }),
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          }
        }
      ).pipe(
        map((r) => JsonConvert.deserializeObject(r, V1x1List)),
        map((l: V1x1List<string>) => l.entries),
        catchError((err, caught) => of([])),
        map((userIds: string[]) => userIds.map(userId => this.getGlobalUser(userId))),
        map(observableUsers => observableUsers.length === 0 ? of([]) : forkJoin(observableUsers)),
        mergeAll())
    ), mergeAll());
  }

  removeUserFromGroup(tenantId: string, groupId: string, userId: string): Observable<V1x1GlobalUser[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.delete(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/users/' + userId,
        this.getAuthorization()
      ).pipe(
        map((r) => this.getGroupUsers(tenantId, groupId)),
        mergeAll())
    ), mergeAll());
  }

  addPermissionToGroup(tenantId: string, groupId: string, permission: string): Observable<string[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.post(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/permissions',
        JsonConvert.serializeObject({
          'value': permission
        }),
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          }
        }
      ).pipe(
        map((r) => JsonConvert.deserializeObject(r, V1x1List)),
        map((l: V1x1List<string>) => l.entries),
        catchError((err, caught) => of([])))
    ), mergeAll());
  }

  removePermissionFromGroup(tenantId: string, groupId: string, permission: string): Observable<string[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.delete(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/permissions/' + permission,
        this.getAuthorization()
      ).pipe(
        map((r) => this.getGroupPermissions(tenantId, groupId)),
        mergeAll())
    ), mergeAll());
  }

  getGroupPermissions(tenantId: string, groupId: string): Observable<string[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/permissions', this.getAuthorization()).pipe(
        map((r) => JsonConvert.deserializeObject(r, V1x1List)),
        map((l: V1x1List<string>) => l.entries),
        catchError((err, caught) => of([])))
    ), mergeAll());
  }

  createGroup(tenantId: string, groupName: string): Observable<V1x1Group> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.post(
        wc.apiBase + '/tenants/' + tenantId + '/groups',
        JsonConvert.serializeObject({
          'value': groupName
        }),
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          }
        }
      )
        .pipe(map((r) => JsonConvert.deserializeObject(r, V1x1Group)))
    ), mergeAll());
  }

  deleteGroup(tenantId: string, groupId: string): Observable<boolean> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.delete(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId,
        this.getAuthorization()
      )
        .pipe(map(r => true))
    ), mergeAll());
  }

  getChannelGroupPlatformMappings(tenantId: string, platform: string, channelGroupId: string): Observable<V1x1ChannelGroupPlatformMapping[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(
        wc.apiBase + '/tenants/' + tenantId + '/channels/' + platform + '/' + channelGroupId + '/mappings',
        this.getAuthorization()
      ).pipe(
        map(r => JsonConvert.deserializeObject(r, V1x1List)),
        map((r: V1x1List<any>) => r.entries.map(e => JsonConvert.deserializeObject(e, V1x1ChannelGroupPlatformMapping))),
        catchError((err, caught) => of([])))
    ), mergeAll());
  }

  getChannelGroupPlatformMappingWrapper(tenantId: string, channelGroup: V1x1ChannelGroup): Observable<V1x1ChannelGroupPlatformMappingWrapper> {
    return this.getChannelGroupPlatformMappings(tenantId, channelGroup.platform, channelGroup.id)
      .pipe(map(r => new V1x1ChannelGroupPlatformMappingWrapper(channelGroup, r)));
  }

  getTenantPlatformMappings(tenant: V1x1Tenant): Observable<V1x1TenantPlatformMapping> {
    return forkJoin(
      tenant.channelGroups.map(
        channelGroup => this.getChannelGroupPlatformMappingWrapper(tenant.id, channelGroup)
      )
    ).pipe(map(r => new V1x1TenantPlatformMapping(r)));
  }

  getPermissionDefinitions(): Observable<V1x1PermissionDefinition[]> {
    return this.webInfo.getWebConfig().pipe(map((wc) =>
      this.http.get(wc.apiBase + '/platform/config-definitions/permission').pipe(
        map(r => JsonConvert.deserializeObject(r, V1x1List)),
        map((r: V1x1List<any>) => r.entries.map(e => JsonConvert.deserializeObject(e, V1x1PermissionDefinition))),
        catchError((err, caught) => {console.log(err, caught); return of([]);}))
    ), mergeAll());
  }
}
