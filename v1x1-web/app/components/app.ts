import {Component} from '@angular/core';
import {V1x1ApiCache} from "../services/api_cache";
import {V1x1TwitchOauthCode} from "../model/v1x1_twitch_oauth_code";
import {V1x1Api} from "../services/api";
import {Observable} from "rxjs";
import {V1x1Tenant} from "../model/v1x1_tenant";
import {V1x1GlobalState} from "../services/global_state";
import {Router} from "@angular/router";
import {UrlId} from "../services/url_id";
import {V1x1WebInfo} from "../services/web_info";

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

  constructor(private apiCache: V1x1ApiCache, private api: V1x1Api, private globalState: V1x1GlobalState, private router: Router, private webInfo: V1x1WebInfo) {
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
      this.postLogin();
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
        this.relogin();
      } else {
        this.globalState.authorization.set(localStorage.getItem("authorization"));
        this.globalState.loggedIn.set(true);
        setTimeout(() => {
          this.relogin();
        }, expiry - new Date().getTime() - 5000);
      }
    }

    if(this.globalState.loggedIn.getCurrent()) {
      this.api.getTenants().subscribe(r => {
        this.globalState.tenants.set(r);
      });
    }
  }

  relogin() {
    this.globalState.loggedIn.set(false);
    localStorage.removeItem("authorization");
    localStorage.removeItem("auth_expiry");
    localStorage.setItem("auth_redirect", window.location.hash);
    localStorage.setItem("auth_redirect_expiry", (new Date().getTime() + 3600).toString(10));
    this.webInfo.getWebConfig().subscribe(
      webConfig => {
        this.api.getState().subscribe(
          state => {
            window.location.href = 'https://api.twitch.tv/kraken/oauth2/authorize' +
              '?response_type=code' +
              '&client_id=' + webConfig.clientIds['TWITCH'] +
              '&redirect_uri=' + webConfig.redirectUris['TWITCH'] +
              '&scope=' +
              'user_read+channel_editor+' +
              'channel_commercial+channel_subscriptions+' +
              'channel_feed_read+channel_feed_edit' +
              '&state=' + state.state;
          }
        );
      }
    );
  }

  postLogin() {
    if(localStorage.getItem("auth_redirect") && localStorage.getItem("auth_redirect_expiry")) {
      let expiry = parseInt(localStorage.getItem("auth_redirect_expiry"), 10);
      if (expiry <= new Date().getTime()) {
        window.location.hash = localStorage.getItem("auth_redirect");
      } else {
        this.navigateDefault();
      }
      localStorage.removeItem("auth_redirect");
      localStorage.removeItem("auth_redirect_expiry");
    } else {
      this.navigateDefault()
    }
  }

  navigateDefault() {
    this.router.navigate(['/', UrlId.fromApi('00000000-0000-0000-0000-000000000000').toUrl(), 'welcome']);
  }
}
