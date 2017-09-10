import {Component} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'permissions-page',
  template: `
    <nav class="navbar navbar-toggleable-md navbar-light bg-faded subnav">
      <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle Navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item" [class.active]="router.isActive(router.createUrlTree(['./groups'], {relativeTo: route}), false)">
            <a class="nav-link" [routerLink]="['./groups']">Groups</a>
          </li>
          <li class="nav-item" [class.active]="router.isActive(router.createUrlTree(['./mapping'], {relativeTo: route}), false)">
            <a class="nav-link" [routerLink]="['./mapping']">Mapping</a>
          </li>
        </ul>
      </div>
    </nav>
    <router-outlet></router-outlet>
  `
})
export class PermissionsPageComponent {
  constructor(private route: ActivatedRoute, private router: Router) {}
}
