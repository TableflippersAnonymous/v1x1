import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
@Component({
  selector: 'configuration-field-value-string',
  template: `<input type="text" value="{{configuration}}" class="form-control" placeholder="{{field.defaultValue}}">`
})
export class ConfigurationFieldValueStringComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public configuration: Object;
}
