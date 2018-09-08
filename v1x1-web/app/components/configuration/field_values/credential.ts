import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/api/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-credential',
  template: `credential`
})
export class ConfigurationFieldValueCredentialComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
}
