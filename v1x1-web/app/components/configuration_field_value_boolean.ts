import {Component, Input, Output, EventEmitter} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "./configurable";
@Component({
  selector: 'configuration-field-value-boolean',
  template: `<label class="form-check-label"><input type="checkbox" [(ngModel)]="configuration" class="form-check-input"> {{field.displayName}}</label>`
})
export class ConfigurationFieldValueBooleanComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
}
