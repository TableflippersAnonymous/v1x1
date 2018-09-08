import {Component, OnDestroy, OnInit} from "@angular/core";
import {V1x1GlobalState} from "../../services/global_state";
import {ActivatedRoute} from "@angular/router";
import {V1x1Tenant} from "../../model/api/v1x1_tenant";
import {UrlId} from "../../services/url_id";
import {Subscription} from "rxjs";

@Component({
  selector: 'nav-router',
  template: `
    <top-nav [loggedIn]="loggedIn" [activeTenantUrl]="activeTenantId" (activeTenantChange)="setActiveTenant($event)">
      <top-nav-entry [justify]="'brand'" [title]="" [route]="[activeTenantId, 'welcome']"></top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Dashboard'" [route]="[activeTenantId, 'dashboard']" *ngIf="loggedIn"></top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Configuration'" [route]="[activeTenantId, 'config']" *ngIf="loggedIn"></top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Permissions'" [route]="[activeTenantId, 'permissions', 'groups']" *ngIf="loggedIn"></top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Logs'" [route]="[activeTenantId, 'logs']" *ngIf="loggedIn && false"></top-nav-entry>
      <top-nav-entry [justify]="'right'" [title]="'Help'" [route]="[activeTenantId, 'help']"></top-nav-entry>
    </top-nav>
    <router-outlet></router-outlet>
  `
})
export class NavRouterComponent implements OnInit, OnDestroy {
  activeTenantId: string = UrlId.fromApi("00000000-0000-0000-0000-000000000000").toUrl();
  loggedIn: boolean = false;
  private subscriptions: Subscription[] = [];

  constructor(private globalState: V1x1GlobalState,
              private route: ActivatedRoute) {}

  ngOnInit() {
    this.subscriptions.push(this.globalState.activeTenant.get().subscribe(tenant => {
      if(tenant !== undefined)
        this.activeTenantId = UrlId.fromApi(tenant.id).toUrl();
      else
        this.activeTenantId = UrlId.fromApi("00000000-0000-0000-0000-000000000000").toUrl();
    }));
    this.subscriptions.push(this.globalState.loggedIn.get().subscribe(loggedIn => {
      this.loggedIn = loggedIn;
    }));
    this.subscriptions.push(this.globalState.tenants.get().subscribe(tenants => {
      this.subscriptions.push(this.route.params.subscribe(params => {
        if(params['tenant_id']) {
          let currentTenant = tenants.find(tenant => tenant.id === UrlId.fromUrl(params['tenant_id']).toApi());
          if(currentTenant)
            this.setActiveTenant(currentTenant);
          else if(tenants.length > 0)
            this.setActiveTenant(tenants[0]);
        }
      }));
    }));
  }

  ngOnDestroy() {
    this.subscriptions.forEach((subscription: Subscription) => subscription.unsubscribe());
  }

  setActiveTenant(tenant: V1x1Tenant) {
    this.globalState.activeTenant.set(tenant);
  }
}
