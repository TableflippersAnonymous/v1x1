import {Input, ContentChild, Directive} from "@angular/core";
import {TopNavEntryContentComponent} from "./top_nav_entry_content";
@Directive({
  selector: 'top-nav-entry'
})
export class TopNavEntryComponent {
  @Input() public justify: 'left' | 'right';
  @Input() public title: string;
  @ContentChild(TopNavEntryContentComponent) public content: TopNavEntryContentComponent;
}
