import {Component} from '@angular/core';
import {V1x1Api} from "../../services/api";
import {V1x1State} from "../../model/v1x1_state";
@Component({
  selector: 'welcome-page',
  template: `
    <div class="jumbotron" style="margin: 1rem;">
      <h1 class="display-3">Welcome!</h1>
      <p class="lead">Hey there!  I'm v1x1.  I make your chat better.</p>
      <hr class="my-4">
      <p>Hi!  So my name is v1x1, and just in case you didn't know, I pronounce that "Vixie".  I'm a chat robot that can
      help you manage your Twitch chat.</p>
      <p><b>What's your name?</b></p>
      <p><button class="btn btn-twitch" (click)="twitchLogin()"><i class="fa fa-twitch"></i> My name is ...</button></p>
    </div>
  `
})
export class WelcomePageComponent {
  state: V1x1State = undefined;
  expires: number = 0;

  constructor(private api: V1x1Api) {
    this.renewState();
  }

  twitchLogin() {
    if(this.expires < new Date().getTime())
      this.api.getState().subscribe(state => this.doTwitchLogin(state));
    this.doTwitchLogin(this.state);
  }

  renewState() {
    this.api.getState().subscribe(state => {
      this.state = state;
      this.expires = new Date().getTime() + this.state.ttl * 0.75;
    });
  }

  doTwitchLogin(state: V1x1State) {
    window.location.href = 'https://api.twitch.tv/kraken/oauth2/authorize' +
        '?response_type=code' +
        '&client_id=' + this.api.getClientId() +
        '&redirect_uri=https://v1x1.tv/' +
        '&scope=' +
          'user_read+channel_editor+' +
          'channel_commercial+channel_subscriptions+' +
          'channel_feed_read+channel_feed_edit' +
        '&state=' + state.state;
  }
}
