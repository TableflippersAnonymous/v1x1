import {Component, Input} from "@angular/core";

@Component({
  selector: 'platform-formatter',
  template: `
    <span [class.color-twitch]="platform === 'TWITCH'" [class.color-discord]="platform === 'DISCORD'">
      <i [class.fa-twitch]="platform === 'TWITCH'" [class.fa-discord]="platform === 'DISCORD'" [class.fab]="true"></i>&nbsp;<ng-content></ng-content>
    </span>
  `
})
export class PlatformFormatterComponent {
  @Input() platform: string;
}
