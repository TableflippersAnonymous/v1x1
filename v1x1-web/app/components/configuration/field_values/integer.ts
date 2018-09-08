import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/api/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-integer',
  template: `
    <mat-form-field>
      <input matInput type="number" class="form-control" [(ngModel)]="configuration" placeholder="{{field.displayName}}">
    </mat-form-field>
  `
})
export class ConfigurationFieldValueIntegerComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
}
