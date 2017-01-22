import {TemplateRef, Directive} from "@angular/core";
@Directive({selector: '[top-nav-entry-content]'})
export class TopNavEntryContentComponent {
  constructor(public templateRef: TemplateRef<any>) {}
}
