import {Component} from '@angular/core';
import {V1x1Module} from "../model/v1x1_module";

@Component({
  selector: 'configuration-page',
  template: `<ngb-tabset class="tabs-left">
    <ngb-tab *ngFor="let v1x1Module of v1x1Modules" [title]="v1x1Module.name">
      <template ngbTabContent>
        <configuration-module [v1x1Module]="v1x1Module"></configuration-module>
      </template>
    </ngb-tab>
  </ngb-tabset>`
})
/*
 <ngb-tab title="Hello World">
 <template ngbTabContent>
 <p>Configuration stuffs</p>
 </template>
 </ngb-tab>
 <ngb-tab title="Echo">
 <template ngbTabContent>
 <p>Configuration stuffs</p>
 </template>
 </ngb-tab>
 */
export class ConfigurationPageComponent {
  v1x1Modules = [new V1x1Module("Hello World"), new V1x1Module("Echo")];
}
