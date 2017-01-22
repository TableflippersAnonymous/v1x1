import {Component, Input, Output} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
@Component({
  selector: 'configuration-field-value-master-enable',
  template: `<label class="form-check-label"><input type="checkbox" [(ngModel)]="configuration" class="form-check-input"> {{field.displayName}}</label>`
})
export class ConfigurationFieldValueMasterEnableComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
}
