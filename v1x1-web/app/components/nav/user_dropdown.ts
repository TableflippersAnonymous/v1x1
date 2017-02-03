import {Component} from "@angular/core";
@Component({
  selector: 'user-dropdown-nav-component',
  template: `
    <li class="nav-item" ngbDropdown *ngIf="loggedIn">
      <a class="nav-link" href="#" id="navbarUserDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ngbDropdownToggle>
        User
      </a>
      <div class="dropdown-menu" style="left: auto; right: 0;" aria-labelledby="navbarUserDropdownMenuLink">
        <span class="dropdown-item"><span class="fa fa-twitch"></span>JacobiCarter</span>
        <span class="dropdown-item"><span class="fa fa-twitch"></span>JacobiCarterAFK</span>
        <span class="dropdown-item">Naomi#8701</span>
        <hr>
        <a class="dropdown-item" href="#">Link/Unlink Users</a>
        <a class="dropdown-item" href="#">Switch Users</a>
      </div>
    </li>
  `
})
export class UserDropdownNavComponent {

}
