import {V1x1Module} from "../model/v1x1_module";
import {Injectable} from "@angular/core";
import {V1x1ConfigurationDefinitionSet} from "../model/v1x1_configuration_definition_set";
import {V1x1ConfigurationDefinition} from "../model/v1x1_configuration_definition";
import {Headers, Http, RequestOptions, Response} from "@angular/http";
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

@Injectable()
export class V1x1Api {
  private v1x1ApiBase: string = '/api/v1';
  private clientId: string = 'cdzt2rwhcwzrf1col1eqaktuntugdct';
  private authorization: string = undefined;

  constructor(private http: Http) {}

  getConfigurationDefinitionList(type: string): Observable<string[]> {
    return this.http.get(this.v1x1ApiBase + '/platform/config-definitions/' + type)
      .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
      .map((l: V1x1List<string>) => l.entries);
  }

  getModuleList(): Observable<string[]> {
    return Observable.forkJoin([
      //this.getConfigurationDefinitionList("global"),
      this.getConfigurationDefinitionList("tenant"),
      this.getConfigurationDefinitionList("channel")
    ]).map(r => [].concat.apply([], r).filter((v, idx, self) => self.indexOf(v) === idx));
  }

  getConfigurationDefinition(type: string, moduleName: string): Observable<V1x1ConfigurationDefinition> {
    return this.http.get(this.v1x1ApiBase + '/platform/config-definitions/' + type + '/' + moduleName)
      .map((r) => JsonConvert.deserializeObject(r.json(), V1x1ConfigurationDefinition)).catch((err, caught) => Observable.of(null));
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
    return this.http.get(this.v1x1ApiBase + '/meta/state').map((r) => JsonConvert.deserializeObject(r.json(), V1x1State));
  }

  getClientId(): string {
    return this.clientId;
  }

  loginTwitch(twitchOauthCode: V1x1TwitchOauthCode): Observable<V1x1AuthToken> {
    return this.http.post(
      this.v1x1ApiBase + '/meta/login/twitch',
      JsonConvert.serializeObject({
        'oauth_code': twitchOauthCode.oauthCode,
        'oauth_state': twitchOauthCode.oauthState
      }),
      new RequestOptions({
        headers: new Headers({
          'Content-Type': 'application/json'
        })
      })
    ).map((r) => JsonConvert.deserializeObject(r.json(), V1x1AuthToken));
  }

  setAuthorization(authorization: string) {
    this.authorization = authorization;
  }

  getAuthorization(): RequestOptions {
    return new RequestOptions({
      headers: new Headers({
        Authorization: this.authorization
      })
    });
  }

  getUserPlatforms(globalUserId: string): Observable<string[]> {
    return this.http.get(this.v1x1ApiBase + "/global-users/" + globalUserId + "/users", this.getAuthorization())
      .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
      .map((l: V1x1List<string>) => l.entries);
  }

  getUserIdsByPlatform(globalUserId: string, platform: string): Observable<string[]> {
    return this.http.get(this.v1x1ApiBase + "/global-users/" + globalUserId + "/users/" + platform, this.getAuthorization())
      .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
      .map((l: V1x1List<string>) => l.entries);
  }

  getUser(globalUserId: string, platform: string, userId: string): Observable<V1x1User> {
    return this.http.get(this.v1x1ApiBase + "/global-users/" + globalUserId + "/users/" + platform + "/" + userId, this.getAuthorization())
      .map((r) => JsonConvert.deserializeObject(r.json(), V1x1User));
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

  getSelf(): Observable<V1x1GlobalUser> {
    return this.getSelfId()
      .map(globalUserId => this.getGlobalUser(globalUserId))
      .mergeAll();
  }

  getSelfId(): Observable<string> {
    return this.http.get(this.v1x1ApiBase + '/meta/self', this.getAuthorization())
      .map(r => JsonConvert.deserializeObject(r.json(), V1x1ApiString))
      .map(r => r.value);
  }

  getTenantIds(): Observable<string[]> {
    return this.http.get(this.v1x1ApiBase + '/tenants', this.getAuthorization())
      .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
      .map((l: V1x1List<string>) => l.entries)
      .map(tenantIds => tenantIds.filter(tenantId => tenantId !== '493073c3-8a6f-38fa-8e38-16af0b436482'));
  }

  getTenantPlatforms(tenantId: string): Observable<string[]> {
    return this.http.get(this.v1x1ApiBase + '/tenants/' + tenantId + '/channels', this.getAuthorization())
      .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
      .map((l: V1x1List<string>) => l.entries)
      .catch((err, caught) => Observable.of([]));
  }

  getChannelIdsByPlatform(tenantId: string, platform: string): Observable<string[]> {
    return this.http.get(this.v1x1ApiBase + '/tenants/' + tenantId + '/channels/' + platform, this.getAuthorization())
      .map((r) => JsonConvert.deserializeObject(r.json(), V1x1List))
      .map((l: V1x1List<string>) => l.entries);
  }

  getChannel(tenantId: string, platform: string, channelId: string): Observable<V1x1Channel> {
    return this.http.get(this.v1x1ApiBase + '/tenants/' + tenantId + '/channels/' + platform + '/' + encodeURIComponent(channelId), this.getAuthorization())
      .map(r => JsonConvert.deserializeObject(r.json(), V1x1Channel));
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
    return this.http.get(this.v1x1ApiBase + '/tenants/' + tenantId + '/config/' + module, this.getAuthorization())
      .map(r => r.json())
      .map(r => new V1x1Configuration(JSON.parse(r.config_json)));
  }
}
