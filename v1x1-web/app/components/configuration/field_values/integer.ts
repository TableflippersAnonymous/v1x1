import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
@Component({
  selector: 'configuration-field-value-integer',
  template: `<input type="number" class="form-control" [(ngModel)]="configuration" placeholder="{{field.defaultValue}}">`
})
export class ConfigurationFieldValueIntegerComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
}
