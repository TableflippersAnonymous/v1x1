import {Component} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'permissions-page',
  template: `
    <mat-toolbar color="primary">
      <span>
        <a mat-button [routerLink]="['./groups']">Groups</a>
      </span>
      <span>
        <a mat-button [routerLink]="['./mapping']">Mapping</a>
      </span>
    </mat-toolbar>
    <router-outlet></router-outlet>
  `
})
export class PermissionsPageComponent {
  constructor(private route: ActivatedRoute, private router: Router) {}
}
