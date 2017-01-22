import {Component, Input, Output} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
@Component({
  selector: 'configuration-field-value-complex',
  template: `
    <div class="container-fluid">
      <configuration-field *ngFor="let complexField of complexFields[field.complexType]" [field]="complexField" [complexFields]="complexFields" [(configuration)]="configuration[complexField.jsonField]"></configuration-field> 
    </div>
  `
})
export class ConfigurationFieldValueComplexComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
}
