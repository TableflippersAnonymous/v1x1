import {Component, OnDestroy, OnInit} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1GlobalState} from "../../services/global_state";
import {Subscription} from "rxjs";
import {V1x1GlobalUser} from "../../model/v1x1_global_user";
import {first} from "rxjs/operators";

@Component({
  selector: 'user-page',
  template: `
    <div class="card-container">
      <form>
        <mat-card *ngIf="globalUser !== undefined">
          <mat-card-header>
            <mat-card-title><h1>User Information</h1></mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="form-group">
              <div class="config-group" style="padding-left: 10px;">
                <div class="input-group">
                  <mat-form-field style="width: 300px;">
                    <input matInput type="text" placeholder="v1x1 ID" readonly value="{{globalUser.id}}">
                  </mat-form-field>
                </div>
                <small class="form-text text-muted">This is the ID of your v1x1 account.  It probably doesn't mean much.</small>
              </div>
            </div>
            <div class="form-group">
              <label>Linked Accounts</label>
              <div class="config-group" style="padding-top: 10px; padding-left: 10px;">
                <div *ngFor="let user of globalUser.users; let i = index" style="margin-bottom: 1rem; padding-left: 10px;" class="config-group">
                  <div class="input-group">
                    <platform-formatter [platform]="user.platform">{{user.displayName}}</platform-formatter>
                    <button mat-button color="warn" style="margin-left: 1rem;" (click)="delIdx(i);">&times;</button>
                  </div>
                </div>
                <button mat-raised-button color="accent" (click)="addList()">+</button><br>
                <small class="form-text text-muted">These are the various accounts associated with this v1x1 account.  Here you can link or unlink accounts as you need.</small>
              </div>
            </div>
          </mat-card-content>
        </mat-card>
      </form>
    </div>
  `
})
export class UserPageComponent implements OnInit, OnDestroy {
  globalUser: V1x1GlobalUser = undefined;
  private subscriptions: Subscription[] = [];

  constructor(private api: V1x1Api, private globalState: V1x1GlobalState) {}

  ngOnInit() {
    this.subscriptions.push(this.api.getSelf().pipe(first()).subscribe(
      (globalUser: V1x1GlobalUser) => {
        this.globalUser = globalUser;
      }
    ));
  }

  ngOnDestroy() {
    this.subscriptions.forEach((subscription: Subscription) => subscription.unsubscribe());
  }
}
