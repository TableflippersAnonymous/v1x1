import {Input, Directive} from "@angular/core";
import {Router} from "@angular/router";
@Directive({
  selector: 'top-nav-entry'
})
export class TopNavEntryComponent {
  @Input() public justify: 'left' | 'right' | 'brand';
  @Input() public title: string;
  private routeValue: any[] = null;

  constructor(private router: Router) {}

  get route() {
    return this.routeValue;
  }

  @Input()
  set route(route: any[]) {
    let shouldNavigate = this.isActive();
    this.routeValue = route;
    if(shouldNavigate)
      this.navigate();
  }

  isActive(): boolean {
    if(this.routeValue === null)
      return false;
    return this.router.isActive(this.router.createUrlTree(this.route), false);
  }

  navigate(): boolean {
    this.router.navigate(this.route);
    return false;
  }
}
