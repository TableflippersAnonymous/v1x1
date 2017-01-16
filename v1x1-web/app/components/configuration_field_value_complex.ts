import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
@Component({
  selector: 'configuration-field-value-complex',
  template: `
    <div class="container-fluid">
      <configuration-field *ngFor="let complexField of complexFields[field.complexType]" [field]="complexField" [complexFields]="complexFields" [configuration]="configuration"></configuration-field> 
    </div>
  `
})
export class ConfigurationFieldValueComplexComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
  @Input() public configuration: Object;
}
