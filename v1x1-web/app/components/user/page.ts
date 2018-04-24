import {Component, OnDestroy, OnInit} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1GlobalState} from "../../services/global_state";
import {Subscription} from "rxjs/Subscription";
import {V1x1GlobalUser} from "../../model/v1x1_global_user";

@Component({
  selector: 'user-page',
  template: `
    <div class="container-fluid" style="margin-top: 1rem;">
      <h1 *ngIf="globalUser === undefined">Loading ...</h1>
      <div *ngIf="globalUser !== undefined">
        <h1>User Information</h1>
        <form>
          <div class="form-group">
            <label>v1x1 ID</label>
            <div class="config-group" style="padding-left: 10px;">
              <div class="input-group">
                <input type="text" class="form-control form-control-plaintext" readonly value="{{globalUser.id}}">
              </div>
              <small class="form-text text-muted">This is the ID of your v1x1 account.  It probably doesn't mean much.</small>
            </div>
          </div>
          <div class="form-group">
            <label>Linked Accounts</label>
            <div class="config-group" style="padding-left: 10px;">
              <div *ngFor="let user of globalUser.users; let i = index" style="margin-bottom: 1rem; padding-left: 10px;" class="config-group">
                <div class="input-group">
                  <platform-formatter [platform]="user.platform">{{user.displayName}}</platform-formatter>
                  <button class="btn btn-danger btn-sm" style="margin-left: 1rem;" (click)="delIdx(i);">&times;</button>
                </div>
              </div>
              <button class="btn btn-warning" (click)="addList()">+</button>
              <small class="form-text text-muted">These are the various accounts associated with this v1x1 account.  Here you can link or unlink accounts as you need.</small>
            </div>
          </div>
        </form>
      </div>
    </div>
  `
})
export class UserPageComponent implements OnInit, OnDestroy {
  globalUser: V1x1GlobalUser = undefined;
  private subscriptions: Subscription[] = [];

  constructor(private api: V1x1Api, private globalState: V1x1GlobalState) {}

  ngOnInit() {
    this.subscriptions.push(this.api.getSelf().first().subscribe(
      (globalUser: V1x1GlobalUser) => {
        this.globalUser = globalUser;
      }
    ));
  }

  ngOnDestroy() {
    this.subscriptions.forEach((subscription: Subscription) => subscription.unsubscribe());
  }
}
