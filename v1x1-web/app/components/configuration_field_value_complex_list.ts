import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
@Component({
  selector: 'configuration-field-value-complex-list',
  template: `
    <configuration-field-value-complex [field]="field" [complexFields]="complexFields" [configuration]="configuration"></configuration-field-value-complex>
    <button class="btn btn-warning">+</button>
  `
})
export class ConfigurationFieldValueComplexListComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
  @Input() public configuration: Object;
}
