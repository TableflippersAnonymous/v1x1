import {V1x1Module} from "../model/v1x1_module";
import {Injectable} from "@angular/core";
import {V1x1ConfigurationDefinitionSet} from "../model/v1x1_configuration_definition_set";
import {V1x1ConfigurationDefinition} from "../model/v1x1_configuration_definition";
import {Headers, Http, RequestOptions} from "@angular/http";
import {Observable} from "rxjs";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {V1x1List} from "../model/v1x1_list";
import {JsonConvert} from "json2typescript";
import {V1x1State} from "../model/v1x1_state";
import {V1x1AuthToken} from "../model/v1x1_auth_token";
import {V1x1TwitchOauthCode} from "../model/v1x1_twitch_oauth_code";
import {V1x1GlobalUser} from "../model/v1x1_global_user";
import {V1x1User} from "../model/v1x1_user";
import {V1x1ApiString} from "../model/v1x1_api_string";
import {V1x1Tenant} from "../model/v1x1_tenant";
import {V1x1Channel} from "../model/v1x1_channel";
import {V1x1Configuration} from "../model/v1x1_configuration";
import {V1x1Group} from "../model/v1x1_group";
import {V1x1GroupMembership} from "../model/v1x1_group_membership";
import {V1x1DisplayNameRecord} from "../model/v1x1_display_name_record";
import {V1x1WebInfo} from "./web_info";
import {V1x1GlobalState} from "./global_state";

@Injectable()
export class V1x1Api {
  constructor(private http: Http, private webInfo: V1x1WebInfo, private globalState: V1x1GlobalState) {}

  getConfigurationDefinitionList(type: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/platform/config-definitions/' + type)
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
    ).mergeAll();
  }

  getModuleList(): Observable<string[]> {
    return Observable.forkJoin([
      //this.getConfigurationDefinitionList("global"),
      this.getConfigurationDefinitionList("tenant"),
      this.getConfigurationDefinitionList("channel")
    ]).map(r => [].concat.apply([], r).filter((v, idx, self) => self.indexOf(v) === idx));
  }

  getConfigurationDefinition(type: string, moduleName: string): Observable<V1x1ConfigurationDefinition> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/platform/config-definitions/' + type + '/' + moduleName)
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1ConfigurationDefinition)).catch((err, caught) => Observable.of(null))
    ).mergeAll();
  }

  getConfigurationDefinitionSet(moduleName: string): Observable<V1x1ConfigurationDefinitionSet> {
    return Observable.zip(
      //this.getConfigurationDefinition("global", moduleName),
      this.getConfigurationDefinition("tenant", moduleName),
      this.getConfigurationDefinition("channel", moduleName),
      (/*global: V1x1ConfigurationDefinition,*/ tenant: V1x1ConfigurationDefinition, channel: V1x1ConfigurationDefinition) => new V1x1ConfigurationDefinitionSet(/*global*/null, tenant, channel)
    );
  }

  getModule(moduleName: string): Observable<V1x1Module> {
    return Observable.zip(
      this.getConfigurationDefinitionSet(moduleName),
      (configDefinitionSet: V1x1ConfigurationDefinitionSet) => new V1x1Module(moduleName, configDefinitionSet)
    );
  }

  getModules(): Observable<V1x1Module[]> {
    return this.getModuleList().map(
      modules =>
        Observable.forkJoin(modules.map(
          moduleName => this.getModule(moduleName)
        ))
    ).mergeAll();
  }

  getState(): Observable<V1x1State> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/meta/state').map((r) => JsonConvert.deserializeObject(r.json(), V1x1State))
    ).mergeAll();
  }

  loginTwitch(twitchOauthCode: V1x1TwitchOauthCode): Observable<V1x1AuthToken> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.post(
        wc.apiBase + '/meta/login/twitch',
        JsonConvert.serializeObject({
          'oauth_code': twitchOauthCode.oauthCode,
          'oauth_state': twitchOauthCode.oauthState
        }),
        new RequestOptions({
          headers: new Headers({
            'Content-Type': 'application/json'
          })
        })
      ).map((r) => JsonConvert.deserializeObject(r.json(), V1x1AuthToken))
    ).mergeAll();
  }

  getAuthorization(): RequestOptions {
    return new RequestOptions({
      headers: new Headers({
        Authorization: this.globalState.authorization.getCurrent()
      })
    });
  }

  getUserPlatforms(globalUserId: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + "/global-users/" + globalUserId + "/users", this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
    ).mergeAll();
  }

  getUserIdsByPlatform(globalUserId: string, platform: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + "/global-users/" + globalUserId + "/users/" + platform, this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
    ).mergeAll();
  }

  getUser(globalUserId: string, platform: string, userId: string): Observable<V1x1User> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + "/global-users/" + globalUserId + "/users/" + platform + "/" + userId, this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1User))
    ).mergeAll();
  }

  getUsersByPlatform(globalUserId: string, platform: string): Observable<V1x1User[]> {
    return this.getUserIdsByPlatform(globalUserId, platform)
      .map(userIds => userIds.map(userId => this.getUser(globalUserId, platform, userId)))
      .map(observableUsers => Observable.forkJoin(observableUsers))
      .mergeAll();
  }

  getUsers(globalUserId: string): Observable<V1x1User[]> {
    return this.getUserPlatforms(globalUserId)
      .map(platforms => platforms.map(platform => this.getUsersByPlatform(globalUserId, platform)))
      .map(observableUsers => Observable.forkJoin(observableUsers))
      .mergeAll()
      .map(users => [].concat.apply([], users));
  }

  getGlobalUser(globalUserId: string): Observable<V1x1GlobalUser> {
    return this.getUsers(globalUserId)
      .map(users => new V1x1GlobalUser(globalUserId, users));
  }

  getGlobalUserByPlatformAndUsername(platform: string, username: string): Observable<V1x1GlobalUser> {
    return this.getDisplayNameRecordByUsername(platform, username)
      .map(displayNameRecord => this.getGlobalUser(displayNameRecord.globalUserId))
      .mergeAll();
  }

  getDisplayNameRecordByUsername(platform: string, username: string): Observable<V1x1DisplayNameRecord> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + "/platform/display-name/" + encodeURIComponent(platform.toLowerCase()) + "/user/by-username/" + encodeURIComponent(username))
        .map(r => JsonConvert.deserializeObject(r.json(), V1x1DisplayNameRecord))
    ).mergeAll();
  }

  getSelf(): Observable<V1x1GlobalUser> {
    return this.getSelfId()
      .map(globalUserId => this.getGlobalUser(globalUserId))
      .mergeAll();
  }

  getSelfId(): Observable<string> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/meta/self', this.getAuthorization())
        .map(r => JsonConvert.deserializeObject(r.json(), V1x1ApiString))
        .map(r => r.value)
    ).mergeAll();
  }

  getTenantIds(): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/tenants', this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
        .map(tenantIds => tenantIds.filter(tenantId => tenantId !== '493073c3-8a6f-38fa-8e38-16af0b436482'))
    ).mergeAll();
  }

  getTenantPlatforms(tenantId: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/channels', this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
        .catch((err, caught) => Observable.of([]))
    ).mergeAll();
  }

  getChannelIdsByPlatform(tenantId: string, platform: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/channels/' + platform, this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
    ).mergeAll();
  }

  getChannel(tenantId: string, platform: string, channelId: string): Observable<V1x1Channel> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/channels/' + platform + '/' + encodeURIComponent(channelId), this.getAuthorization())
        .map(r => JsonConvert.deserializeObject(r.json(), V1x1Channel))
    ).mergeAll();
  }

  getChannelsByPlatform(tenantId: string, platform: string): Observable<V1x1Channel[]> {
    return this.getChannelIdsByPlatform(tenantId, platform)
      .map(channelIds => channelIds.map(channelId => this.getChannel(tenantId, platform, channelId)))
      .map(observableChannels => Observable.forkJoin(observableChannels))
      .mergeAll();
  }

  getChannels(tenantId: string): Observable<V1x1Channel[]> {
    return this.getTenantPlatforms(tenantId)
      .map(platforms => platforms.map(platform => this.getChannelsByPlatform(tenantId, platform)))
      .map(observableChannels => Observable.forkJoin(observableChannels))
      .mergeAll()
      .map(channels => [].concat.apply([], channels));
  }

  getTenant(tenantId: string): Observable<V1x1Tenant> {
    return this.getChannels(tenantId)
      .map(channels => channels.length === 0 ? null : new V1x1Tenant(tenantId, channels));
  }

  getTenants(): Observable<V1x1Tenant[]> {
    return this.getTenantIds()
      .map(tenantIds => tenantIds.map(tenantId => this.getTenant(tenantId)))
      .map(observableTenants => Observable.forkJoin(observableTenants))
      .mergeAll()
      .map(tenants => tenants.filter(tenant => tenant !== null));
  }

  getTenantConfiguration(tenantId: string, module: string): Observable<V1x1Configuration> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/config/' + module, this.getAuthorization())
        .map(r => r.json())
        .map(r => new V1x1Configuration(JSON.parse(r.config_json)))
    ).mergeAll();
  }

  putTenantConfiguration(tenantId: string, module: string, config: V1x1Configuration): Observable<V1x1Configuration> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.put(
        wc.apiBase + '/tenants/' + tenantId + '/config/' + module,
        JsonConvert.serializeObject({
          'config_json': JSON.stringify(config.configuration)
        }),
        new RequestOptions({
          headers: new Headers({
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          })
        }))
          .map(r => r.json())
          .map(r => new V1x1Configuration(JSON.parse(r.config_json)))
    ).mergeAll();
  }

  getTenantGroupWithMemberships(tenantId: string): Observable<V1x1GroupMembership[]> {
    return this.getGroupIds(tenantId)
      .map(groupIds => groupIds.map(groupId => this.getGroupMembership(tenantId, groupId)))
      .map(observableGroups => Observable.forkJoin(observableGroups))
      .mergeAll();
  }

  getGroupIds(tenantId: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/groups', this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
        .catch((err, caught) => Observable.of([]))
    ).mergeAll();
  }

  getGroupMembership(tenantId: string, groupId: string): Observable<V1x1GroupMembership> {
    return Observable.zip(
      this.getGroup(tenantId, groupId),
      this.getGroupUsers(tenantId, groupId),
      (group, users) => new V1x1GroupMembership(group, users)
    );
  }

  getGroup(tenantId: string, groupId: string): Observable<V1x1Group> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId, this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1Group))
    ).mergeAll();
  }

  getGroupUsers(tenantId: string, groupId: string): Observable<V1x1GlobalUser[]> {
    return this.getGroupUserIds(tenantId, groupId)
      .map(userIds => userIds.map(userId => this.getGlobalUser(userId)))
      .map(observableUsers => observableUsers.length === 0 ? Observable.of([]) : Observable.forkJoin(observableUsers))
      .mergeAll();
  }

  getGroupUserIds(tenantId: string, groupId: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/users', this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
        .catch((err, caught) => Observable.of([]))
    ).mergeAll();
  }

  addUserToGroup(tenantId: string, groupId: string, userId: string): Observable<V1x1GlobalUser[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.post(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/users',
        JsonConvert.serializeObject({
          'value': userId
        }),
        new RequestOptions({
          headers: new Headers({
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          })
        })
      )
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
        .catch((err, caught) => Observable.of([]))
        .map(userIds => userIds.map(userId => this.getGlobalUser(userId)))
        .map(observableUsers => observableUsers.length === 0 ? Observable.of([]) : Observable.forkJoin(observableUsers))
        .mergeAll()
    ).mergeAll();
  }

  removeUserFromGroup(tenantId: string, groupId: string, userId: string): Observable<V1x1GlobalUser[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.delete(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/users/' + userId,
        this.getAuthorization()
      )
        .map((r) => this.getGroupUsers(tenantId, groupId))
        .mergeAll()
    ).mergeAll();
  }

  addPermissionToGroup(tenantId: string, groupId: string, permission: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.post(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/permissions',
        JsonConvert.serializeObject({
          'value': permission
        }),
        new RequestOptions({
          headers: new Headers({
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          })
        })
      )
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
        .catch((err, caught) => Observable.of([]))
    ).mergeAll();
  }

  removePermissionFromGroup(tenantId: string, groupId: string, permission: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.delete(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/permissions/' + permission,
        this.getAuthorization()
      )
        .map((r) => this.getGroupPermissions(tenantId, groupId))
        .mergeAll()
    ).mergeAll();
  }

  getGroupPermissions(tenantId: string, groupId: string): Observable<string[]> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.get(wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId + '/permissions', this.getAuthorization())
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
        .map((l: V1x1List<string>) => l.entries)
        .catch((err, caught) => Observable.of([]))
    ).mergeAll();
  }

  createGroup(tenantId: string, groupName: string): Observable<V1x1Group> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.post(
        wc.apiBase + '/tenants/' + tenantId + '/groups',
        JsonConvert.serializeObject({
          'value': groupName
        }),
        new RequestOptions({
          headers: new Headers({
            'Content-Type': 'application/json',
            Authorization: this.globalState.authorization.getCurrent()
          })
        })
      )
        .map((r) => JsonConvert.deserializeObject(r.json(), V1x1Group))
    ).mergeAll();
  }

  deleteGroup(tenantId: string, groupId: string): Observable<boolean> {
    return this.webInfo.getWebConfig().map((wc) =>
      this.http.delete(
        wc.apiBase + '/tenants/' + tenantId + '/groups/' + groupId,
        this.getAuthorization()
      )
        .map(r => true)
    ).mergeAll();
  }
}
