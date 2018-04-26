import {Component} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'permissions-page',
  template: `
    <nav class="navbar navbar-light bg-light subnav">
      <ul class="nav">
        <li class="nav-item" [class.active]="router.isActive(router.createUrlTree(['./groups'], {relativeTo: route}), false)">
          <a class="nav-link" [routerLink]="['./groups']">Groups</a>
        </li>
        <li class="nav-item" [class.active]="router.isActive(router.createUrlTree(['./mapping'], {relativeTo: route}), false)">
          <a class="nav-link" [routerLink]="['./mapping']">Mapping</a>
        </li>
      </ul>
    </nav>
    <router-outlet></router-outlet>
  `
})
export class PermissionsPageComponent {
  constructor(private route: ActivatedRoute, private router: Router) {}
}
