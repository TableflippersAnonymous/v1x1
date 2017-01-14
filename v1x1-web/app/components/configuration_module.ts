import {Component, Input} from '@angular/core';
import {V1x1Module} from "../model/v1x1_module";

@Component({
  selector: 'configuration-module',
  template: `<p>{{v1x1Module.name}} Configuration stuffs</p>`
})
export class ConfigurationModuleComponent {
  @Input() public v1x1Module: V1x1Module;
}
