import {Component, ContentChildren, QueryList} from "@angular/core";
import {TopNavEntryComponent} from "./top_nav_entry";
@Component({
  selector: 'top-nav',
  template: `
    <nav class="navbar navbar-toggleable-md navbar-light bg-faded">
      <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle Navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <a class="v1x1-color navbar-brand" href="#">v1x1</a>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
          <li *ngFor="let item of entriesOn('left'); let i = index" class="nav-item" [class.active]="activeIdx === i * 2">
            <a class="nav-link" href="#" (click)="setActive(i * 2);">{{item.title}}</a>
          </li>
        </ul>
        <ul class="navbar-nav ml-auto">
          <li *ngFor="let item of entriesOn('right'); let i = index;" class="nav-item" [class.active]="activeIdx === i * 2 + 1">
            <a class="nav-link" href="#" (click)="setActive(i * 2 + 1);">{{item.title}}</a>
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
    <div *ngFor="let item of entriesOn('left'); let i = index">
      <template *ngIf="activeIdx === i * 2" [ngTemplateOutlet]="item.content.templateRef">{{i}}</template>
    </div>
    <div *ngFor="let item of entriesOn('right'); let i = index">
      <template *ngIf="activeIdx === i * 2 + 1" [ngTemplateOutlet]="item.content.templateRef">{{i}}</template>
    </div>
  `
})
export class TopNavComponent {
  @ContentChildren(TopNavEntryComponent) entries: QueryList<TopNavEntryComponent>;
  activeIdx = 0;

  entriesOn(side: string) {
    return this.entries.filter((x, i, a) => x.justify === side);
  }

  setActive(idx: number) {
    this.activeIdx = idx;
  }
}