import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-complex',
  template: `
    <configuration-field *ngFor="let complexField of complexFields[field.complexType]"
                         [field]="complexField" [complexFields]="complexFields"
                         [originalConfiguration]="originalConfiguration[complexField.jsonField]"
                         [configuration]="configuration[complexField.jsonField]" (configurationChange)="setConfigField(complexField.jsonField, $event)"></configuration-field>
  `
})
export class ConfigurationFieldValueComplexComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
}
