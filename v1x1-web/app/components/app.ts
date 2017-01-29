import {Component} from '@angular/core';
import {V1x1ApiCache} from "../services/api_cache";

@Component({
  selector: 'v1x1-app',
  template: `
    <top-nav>
      <top-nav-entry [justify]="'left'" [title]="'Dashboard'">
        <template top-nav-entry-content>
          <dashboard-page></dashboard-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Configuration'">
        <template top-nav-entry-content>
          <configuration-page></configuration-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Permissions'">
        <template top-nav-entry-content>
          <permissions-page></permissions-page>
        </template>
      </top-nav-entry>
      <top-nav-entry [justify]="'left'" [title]="'Logs'">
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

  constructor(private api: V1x1ApiCache) {
    this.api.preload();
  }
}
