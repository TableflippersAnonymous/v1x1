import {Component, Input} from "@angular/core";
import {V1x1Module} from "../model/v1x1_module";
import {V1x1ConfigurationDefinition} from "../model/v1x1_configuration_definition";
import {V1x1Configuration} from "../model/v1x1_configuration";
@Component({
  selector: 'configuration-scope',
  template: `
    <configuration-field *ngFor="let field of configurationDefinition.fields" [field]="field" [configuration]="configuration[field.jsonField]"></configuration-field>
`
})
export class ConfigurationScopeComponent {
  @Input() public v1x1Module: V1x1Module;
  @Input() public configurationDefinition: V1x1ConfigurationDefinition;
  @Input() public configuration: V1x1Configuration;
  @Input() public scope: string;
  @Input() public id: string;

}
