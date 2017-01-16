import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
@Component({
  selector: 'configuration-field-value-integer',
  template: `<input type="number" class="form-control" value="{{configuration}}" placeholder="{{field.defaultValue}}">`
})
export class ConfigurationFieldValueIntegerComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public configuration: Object;
}
