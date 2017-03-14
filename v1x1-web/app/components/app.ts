import {Component} from '@angular/core';
import {V1x1ApiCache} from "../services/api_cache";
import {V1x1TwitchOauthCode} from "../model/v1x1_twitch_oauth_code";
import {V1x1Api} from "../services/api";
import {Observable} from "rxjs";
import {V1x1Tenant} from "../model/v1x1_tenant";
import {V1x1GlobalState} from "../services/global_state";
import {Router} from "@angular/router";
import {UrlId} from "../services/url_id";

@Component({
  selector: 'v1x1-app',
  template: `
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  name = 'v1x1';
  queryString: {[key: string]: string} = {};
  loggingIn: boolean = false;
  activeTenant: V1x1Tenant = null;
  activeTenantId: string = "";

  constructor(private apiCache: V1x1ApiCache, private api: V1x1Api, private globalState: V1x1GlobalState, private router: Router) {
    this.apiCache.preload();

    if(window.location.search) {
      window.location.search.substring(1).split('&').forEach(e => {
        let element = e.split('=', 2);
        this.queryString[element[0]] = decodeURIComponent(element[1]);
      });
    }

    if(this.queryString['code'] && this.queryString['state'])
      this.handleLogin(new V1x1TwitchOauthCode(this.queryString['code'], this.queryString['state']));
    else
      this.loginFromLocalStorage();

    if(this.globalState.loggedIn.getCurrent()) {
      this.api.getTenants().subscribe(r => {
        this.globalState.tenants.set(r);
      });
    }
  }

  handleLogin(oauthCode: V1x1TwitchOauthCode) {
    this.loggingIn = true;
    this.api.loginTwitch(oauthCode).catch((_err, _caught) => {
      this.loggingIn = false;
      return Observable.of(null);
    }).subscribe(authToken => {
      if(authToken === null)
        return;
      localStorage.setItem("authorization", authToken.authorization);
      localStorage.setItem("auth_expiry", authToken.expires);
      this.loginFromLocalStorage();
      if(history && history.pushState)
        history.pushState(null, null, '/');
      this.router.navigate(['/', UrlId.fromApi('00000000-0000-0000-0000-000000000000').toUrl(), 'welcome']);
    })
  }

  setActiveTenant(tenant: V1x1Tenant) {
    this.activeTenant = tenant;
    this.activeTenantId = tenant.id;
    this.globalState.activeTenant.set(tenant);
  }

  loginFromLocalStorage() {
    if(localStorage.getItem("authorization") && localStorage.getItem("auth_expiry")) {
      let expiry = parseInt(localStorage.getItem("auth_expiry"), 10);
      if (expiry <= new Date().getTime()) {
        localStorage.removeItem("authorization");
        localStorage.removeItem("auth_expiry");
      } else {
        this.api.setAuthorization(localStorage.getItem("authorization"));
        this.globalState.loggedIn.set(true);
        setTimeout(() => {
          this.globalState.loggedIn.set(false);
          this.router.navigate(['/', UrlId.fromApi('00000000-0000-0000-0000-000000000000').toUrl(), 'welcome']);
          localStorage.removeItem("authorization");
          localStorage.removeItem("auth_expiry");
        }, expiry - new Date().getTime() - 5000);
      }
    }
  }
}
