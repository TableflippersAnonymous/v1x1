import {Component, ContentChildren, EventEmitter, Input, OnInit, Output, QueryList} from "@angular/core";
import {TopNavEntryComponent} from "./top_nav_entry";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'top-nav',
  template: `
    <mat-toolbar color="primary">
      <span>
        <a mat-button href="#" (click)="entriesOn('brand')[0].navigate();">v1x1</a>
      </span>
      <span *ngFor="let item of entriesOn('left'); let i = index" class="nav-item" [class.active]="item.isActive()">
        <a mat-button href="#" (click)="item.navigate();">{{item.title}}</a>
      </span>
      <span style="flex: 1 1 auto;"></span>
      <span *ngFor="let item of entriesOn('right'); let i = index;" class="nav-item" [class.active]="item.isActive()">
        <a mat-button href="#" (click)="item.navigate();">{{item.title}}</a>
      </span>
      <tenant-dropdown-nav-component *ngIf="loggedIn" (activeTenantChange)="activeTenantChange.emit($event)"></tenant-dropdown-nav-component>
      <user-dropdown-nav-component *ngIf="loggedIn" [activeTenantUrl]="activeTenantUrl"></user-dropdown-nav-component>
    </mat-toolbar>
  `
})
export class TopNavComponent implements OnInit {
  @Input() loggedIn: boolean;
  @Input() activeTenantUrl: string;
  @ContentChildren(TopNavEntryComponent) entries: QueryList<TopNavEntryComponent>;
  @Output() public activeTenantChange = new EventEmitter();

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {}

  entriesOn(side: string) {
    return this.entries.filter((x, i, a) => x.justify === side);
  }
}
