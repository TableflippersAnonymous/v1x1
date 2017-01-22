import {Component} from '@angular/core';

@Component({
  selector: 'v1x1-app',
  /*template: `<nav class="navbar navbar-toggleable-md navbar-light bg-faded">
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle Navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <a class="v1x1-color navbar-brand" href="#">v1x1</a>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item active">
          <a class="nav-link" href="#">Dashboard</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">Configuration</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">Permissions</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">Logs</a>
        </li>
      </ul>
      <ul class="navbar-nav ml-auto">
        <li class="nav-item">
          <a class="nav-link" href="#">Help</a>
        </li>
        <li class="nav-item" ngbDropdown>
          <a class="nav-link" href="#" id="navbarTenantDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ngbDropdownToggle>
            Tenant
          </a>
          <div class="dropdown-menu" style="left: auto; right: 0;" aria-labelledby="navbarTenantDropdownMenuLink">
            <a class="dropdown-item active" href="#"><span class="fa fa-twitch"></span>/draskia,<br>Draskia's Potato Den</a>
            <a class="dropdown-item" href="#"><span class="fa fa-twitch"></span>/v1x1,<br><span class="fa fa-twitch"></span>/v0x1</a>
            <hr>
            <a class="dropdown-item" href="#">+ Create new</a>
          </div>
        </li>
        <li class="nav-item" ngbDropdown>
          <a class="nav-link" href="#" id="navbarUserDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ngbDropdownToggle>
            User
          </a>
          <div class="dropdown-menu" style="left: auto; right: 0;" aria-labelledby="navbarUserDropdownMenuLink">
            <span class="dropdown-item"><span class="fa fa-twitch"></span>JacobiCarter</span>
            <span class="dropdown-item"><span class="fa fa-twitch"></span>JacobiCarterAFK</span>
            <span class="dropdown-item">Cobi#8701</span>
            <hr>
            <a class="dropdown-item" href="#">Link/Unlink Users</a>
            <a class="dropdown-item" href="#">Switch Users</a>
          </div>
        </li>
      </ul>
    </div>
  </nav>
  <configuration-page *ngIf=""></configuration-page>`,*/
  /* */
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

}
