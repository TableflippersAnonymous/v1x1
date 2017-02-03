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

@Injectable()
export class V1x1Api {
  private v1x1ApiBase: string = '/api/v1';
  private clientId: string = 'cdzt2rwhcwzrf1col1eqaktuntugdct';
  private authorization: string = undefined;

  constructor(private http: Http) {}

  getConfigurationDefinitionList(type: string): Observable<string[]> {
    return this.http.get(this.v1x1ApiBase + '/platform/config-definitions/' + type).map((r) => JsonConvert.deserializeObject(r.json(), V1x1List)).map((l: V1x1List<string>) => l.entries);
  }

  getModuleList(): Observable<string[]> {
    return Observable.forkJoin([
      this.getConfigurationDefinitionList("global"),
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
      this.getConfigurationDefinition("global", moduleName),
      this.getConfigurationDefinition("tenant", moduleName),
      this.getConfigurationDefinition("channel", moduleName),
      (global: V1x1ConfigurationDefinition, tenant: V1x1ConfigurationDefinition, channel: V1x1ConfigurationDefinition) => new V1x1ConfigurationDefinitionSet(global, tenant, channel)
    );
  }

  getModule(moduleName: string): Observable<V1x1Module> {
    return Observable.zip(
      this.getConfigurationDefinitionSet(moduleName),
      (configDefinitionSet: V1x1ConfigurationDefinitionSet) => new V1x1Module(moduleName, configDefinitionSet.tenant.display_name, configDefinitionSet.tenant.description, configDefinitionSet)
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
    return this.getUserPlatforms(this.v1x1ApiBase + "/global-users/" + globalUserId)
      .map(platforms => platforms.map(platform => this.getUsersByPlatform(globalUserId, platform)))
      .map(observableUsers => Observable.forkJoin(observableUsers))
      .map(users => [].concat.apply([], users))
      .mergeAll();
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
    return this.http.get(
      this.v1x1ApiBase + '/meta/self',
      this.getAuthorization()
    ).catch((err, caught) => {
      if(err instanceof Response) {
        if(err.status === 307) {
          return Observable.of(err.headers.get("Location"))
        }
        return Observable.of(null);
      }
    }).map(r => {
      let url;
      if (r instanceof Response)
        if (r.status == 307)
          url = r.headers.get("Location");
        else
          throw new Error("No URL from self");
      else
        throw new Error("No URL from self");
      return url.split("/").reverse()[0];
    });
  }
}
