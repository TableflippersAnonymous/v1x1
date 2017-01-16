import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
@Component({
  selector: 'configuration-field-value-master-enable',
  template: `<label class="form-check-label"><input type="checkbox" checked="{{configuration}}" class="form-check-input"> {{field.displayName}}</label>`
})
export class ConfigurationFieldValueMasterEnableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public configuration: Object;
}
