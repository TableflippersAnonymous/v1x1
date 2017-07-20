import {Component, Input} from '@angular/core';
import {V1x1Api} from "../../services/api";
import {V1x1State} from "../../model/v1x1_state";
import {V1x1WebInfo} from "../../services/web_info";
import {Observable} from "rxjs";
@Component({
  selector: 'welcome-page',
  template: `
    <div class="jumbotron" style="margin: 1rem;">
      <h1 class="display-3">Welcome!</h1>
      <p class="lead">I'm v1x1. I make your chat better.</p>
      <hr class="my-4">
      <p>Hi! My name is v1x1 ("Vixie"), a chat robot that can help you manage your Twitch chat!</p>
      <div *ngIf="!loggedIn">
        <p><b>What's your name?</b></p>
        <p><button class="btn btn-twitch" (click)="doTwitchLogin()"><i class="fa fa-twitch"></i> My name is ...</button></p>
      </div>
    </div>
  `
})
export class WelcomePageComponent {
  state: V1x1State = undefined;
  expires: number = 0;
  @Input() loggedIn: boolean;

  constructor(private api: V1x1Api, private webInfo: V1x1WebInfo) {
    this.renewState();
  }

  renewState() {
    this.api.getState().subscribe(state => {
      this.state = state;
      this.expires = new Date().getTime() + this.state.ttl * 0.75;
    });
  }

  getState(): Observable<string> {
    if(this.expires < new Date().getTime())
      return this.api.getState().map(s => s.state);
    else
      return Observable.of(this.state.state);
  }

  doTwitchLogin() {
    Observable.zip(
      this.webInfo.getWebConfig(),
      this.getState()
    ).subscribe(
      ([wc, state]) =>
        window.location.href = 'https://api.twitch.tv/kraken/oauth2/authorize' +
          '?response_type=code' +
          '&client_id=' + wc.clientIds['TWITCH'] +
          '&redirect_uri=' + wc.redirectUris['TWITCH'] +
          '&scope=' +
            'user_read+channel_editor+' +
            'channel_commercial+channel_subscriptions+' +
            'channel_feed_read+channel_feed_edit' +
          '&state=' + state
    );
  }
}
