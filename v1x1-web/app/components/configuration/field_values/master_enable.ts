import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/api/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-master-enable',
  template: `<mat-slide-toggle [(ngModel)]="configuration">{{field.displayName}}</mat-slide-toggle>`
})
export class ConfigurationFieldValueMasterEnableComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
}
