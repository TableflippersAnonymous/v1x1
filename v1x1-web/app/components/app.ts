import {Component} from '@angular/core';

@Component({
  selector: 'v1x1-app',
  template: `<nav class="navbar navbar-toggleable-md navbar-light bg-faded">
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle Navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <a class="navbar-brand" href="#">v1x1</a>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ">
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
          <a class="nav-link" href="#" id="navbarTenantDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ngbDropdownToggle>
            User
          </a>
          <div class="dropdown-menu" style="left: auto; right: 0;" aria-labelledby="navbarTenantDropdownMenuLink">
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
  <config></config>`,
})
export class AppComponent { name = 'v1x1'; }
