import {Component} from '@angular/core';
import {V1x1ApiCache} from "../services/api_cache";
import {V1x1TwitchOauthCode} from "../model/v1x1_twitch_oauth_code";
import {V1x1Api} from "../services/api";
import {Observable} from "rxjs";
import {V1x1Tenant} from "../model/v1x1_tenant";

@Component({
  selector: 'v1x1-app',
  template: `
    <top-nav [loggedIn]="loggedIn" (activeTenantChange)="setActiveTenant($event)">
      <top-nav-entry [justify]="'brand'" [title]="">
        <template top-nav-entry-content>
          <welcome-page></welcome-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Dashboard'" *ngIf="loggedIn && false">
        <template top-nav-entry-content>
          <dashboard-page></dashboard-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Configuration'" *ngIf="loggedIn">
        <template top-nav-entry-content>
          <configuration-page [activeTenant]="activeTenant"></configuration-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Permissions'" *ngIf="loggedIn && false">
        <template top-nav-entry-content>
          <permissions-page></permissions-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Logs'" *ngIf="loggedIn && false">
        <template top-nav-entry-content>
          <logs-page></logs-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'right'" [title]="'Help'">
        <template top-nav-entry-content>
          <help-page></help-page>
        </template>
      </top-nav-entry>
    </top-nav>
  `
})
export class AppComponent {
  name = 'v1x1';
  queryString: {[key: string]: string} = {};
  loggedIn: boolean = false;
  loggingIn: boolean = false;
  activeTenant: V1x1Tenant = null;

  constructor(private apiCache: V1x1ApiCache, private api: V1x1Api) {
    this.apiCache.preload();

    if(window.location.search) {
      window.location.search.substring(1).split('&').forEach(e => {
        let element = e.split('=', 2);
        this.queryString[element[0]] = decodeURIComponent(element[1]);
      });
    }

    if(this.queryString['code'] && this.queryString['state'])
      this.handleLogin(new V1x1TwitchOauthCode(this.queryString['code'], this.queryString['state']));

    if(localStorage.getItem("authorization") && localStorage.getItem("auth_expiry")) {
      let expiry = parseInt(localStorage.getItem("auth_expiry"), 10);
      if (expiry <= new Date().getTime()) {
        localStorage.removeItem("authorization");
        localStorage.removeItem("auth_expiry");
      } else {
        this.api.setAuthorization(localStorage.getItem("authorization"));
        this.loggedIn = true;
        setTimeout(() => {
          this.loggedIn = false;
          localStorage.removeItem("authorization");
          localStorage.removeItem("auth_expiry");
        }, expiry - new Date().getTime() - 5000);
      }
    }
  }

  handleLogin(oauthCode: V1x1TwitchOauthCode) {
    this.loggingIn = true;
    this.api.loginTwitch(oauthCode).catch((err, caught) => {
      this.loggingIn = false;
      return Observable.of(null);
    }).subscribe(authToken => {
      if(authToken === null)
        return;
      localStorage.setItem("authorization", authToken.authorization);
      localStorage.setItem("auth_expiry", authToken.expires);
      window.location.href = '/';
    })
  }

  setActiveTenant(tenant: V1x1Tenant) {
    this.activeTenant = tenant;
  }
}
