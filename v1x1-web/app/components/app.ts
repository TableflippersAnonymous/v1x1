import {Component} from '@angular/core';
import {V1x1ApiCache} from "../services/api_cache";
import {V1x1TwitchOauthCode} from "../model/v1x1_twitch_oauth_code";
import {V1x1Api} from "../services/api";

@Component({
  selector: 'v1x1-app',
  template: `
    <top-nav [loggedIn]="loggedIn">
      <top-nav-entry [justify]="'brand'" [title]="">
        <template top-nav-entry-content>
          <welcome-page></welcome-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Dashboard'" *ngIf="loggedIn">
        <template top-nav-entry-content>
          <dashboard-page></dashboard-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Configuration'" *ngIf="loggedIn">
        <template top-nav-entry-content>
          <configuration-page></configuration-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Permissions'" *ngIf="loggedIn">
        <template top-nav-entry-content>
          <permissions-page></permissions-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Logs'" *ngIf="loggedIn">
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
  }

  handleLogin(oauthCode: V1x1TwitchOauthCode) {
    this.api.loginTwitch(oauthCode).subscribe(authToken => {
      this.api.setAuthorization(authToken.authorization);
      this.loggedIn = true;
    });
  }
}
