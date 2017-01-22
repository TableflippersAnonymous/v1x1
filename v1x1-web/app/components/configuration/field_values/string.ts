import {Component, Input, Output} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
@Component({
  selector: 'configuration-field-value-string',
  template: `<input type="text" [(ngModel)]="configuration" class="form-control" placeholder="{{field.defaultValue}}">`
})
export class ConfigurationFieldValueStringComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
}
