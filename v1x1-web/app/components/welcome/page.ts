import {Component, OnDestroy, OnInit} from '@angular/core';
import {V1x1Api} from "../../services/api";
import {V1x1State} from "../../model/v1x1_state";
import {V1x1WebInfo} from "../../services/web_info";
import {Observable} from "rxjs";
import {Subscription} from "rxjs/Subscription";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1GlobalUser} from "../../model/v1x1_global_user";

@Component({
  selector: 'welcome-page',
  template: `
    <div class="jumbotron" style="margin: 1rem;">
      <h1 *ngIf="displayName === ''" class="display-3">Welcome!</h1>
      <h1 *ngIf="displayName !== ''" class="display-3">Welcome, {{displayName}}!</h1>
      <p class="lead">I'm v1x1. I make your chat better.</p>
      <hr class="my-4">
      <p *ngIf="!loggedIn">Hi! My name is v1x1 ("Vixie"), a chat robot that can help you manage your Twitch chat!</p>
      <div *ngIf="!loggedIn">
        <p><b>What's your name?</b></p>
        <p><button class="btn btn-twitch" (click)="doTwitchLogin()"><i class="fab fa-twitch"></i> My name is ...</button>
          <button class="btn btn-discord" (click)="doDiscordLogin(true)"><i class="fab fa-discord"></i> Please join my Discord server ...</button>
          <button class="btn btn-discord" (click)="doDiscordLogin(false)"><i class="fab fa-discord"></i> My name is ...</button>
          <!--button class="btn btn-slack" (click)="doSlackLogin()"><i class="fab fa-slack"></i> My name is ...</button>
          <button class="btn btn-youtube" (click)="doYouTubeLogin()"><i class="fab fa-youtube"></i> My name is ...</button>
          <button class="btn btn-mixer" (click)="doMixerLogin()"><svg class="svg-inline--fa fa-mixer fa-w-18" aria-hidden="true" data-fa-processed="" data-prefix="fab" data-icon="mixer" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path fill="currentColor" d="M116.03,77.68c-15.76-21.29-46.72-24.61-66.91-6.36c-17.42,16.04-18.8,43.13-4.7,62.21l90.96,121.92L43.87,378.48 c-14.1,19.08-12.99,46.17,4.7,62.21c20.18,18.25,51.15,14.93,66.91-6.36l127.73-171.69c3.04-4.15,3.04-9.95,0-14.1L116.03,77.68z"/><path fill="currentColor" d="M396.37,77.68c15.76-21.29,46.72-24.61,66.91-6.36c17.42,16.04,18.8,43.13,4.7,62.21l-90.96,121.92l91.51,123.03 c14.1,19.08,12.99,46.17-4.7,62.21c-20.18,18.25-51.15,14.93-66.91-6.36L269.47,262.36c-3.04-4.15-3.04-9.95,0-14.1L396.37,77.68z"/></svg> My name is ...</button--></p>
      </div>
      <div *ngIf="loggedIn">
        <p>To get started, check out the Configuration tab above.</p>
      </div>
    </div>
  `
})
export class WelcomePageComponent implements OnInit, OnDestroy {
  state: V1x1State = undefined;
  expires: number = 0;
  loggedIn: boolean = false;
  subscriptions: Subscription[] = [];
  displayName: string = "";

  constructor(private api: V1x1Api, private globalState: V1x1GlobalState, private webInfo: V1x1WebInfo) {}

  ngOnInit() {
    this.subscriptions.push(this.globalState.loggedIn.get().subscribe(loggedIn => {
      this.loggedIn = loggedIn;
      if(loggedIn)
        this.subscriptions.push(this.getDisplayName().subscribe(displayName => {
          this.displayName = displayName;
        }));
    }));
    this.renewState();
  }

  ngOnDestroy() {
    this.subscriptions.forEach((subscription: Subscription) => subscription.unsubscribe());
  }

  renewState() {
    this.subscriptions.push(this.api.getState().first().subscribe(state => {
      this.state = state;
      this.expires = new Date().getTime() + this.state.ttl * 0.75;
    }));
  }

  getState(): Observable<string> {
    if(this.expires < new Date().getTime())
      return this.api.getState().map(s => s.state);
    else
      return Observable.of(this.state.state);
  }

  doTwitchLogin() {
    localStorage.setItem("auth_in_progress", "twitch");
    this.subscriptions.push(Observable.zip(
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
    ));
  }

  doDiscordLogin(bot: boolean) {
    localStorage.setItem("auth_in_progress", "discord");
    this.subscriptions.push(Observable.zip(
      this.webInfo.getWebConfig(),
      this.getState()
    ).subscribe(
      ([wc, state]) =>
        window.location.href = 'https://discordapp.com/api/oauth2/authorize' +
          '?response_type=code' +
          '&client_id=' + wc.clientIds['DISCORD'] +
          '&redirect_uri=' + wc.redirectUris['DISCORD'] +
          '&scope=' +
          'identify+connections' +
          (bot ? '+bot' : '') +
          '&state=' + state +
          (bot ? '&permissions=234355910' : '')
    ));
  }

  getDisplayName(): Observable<string> {
    return this.api.getSelf().map((user: V1x1GlobalUser) => user.users[0].displayName).share();
  }
}
