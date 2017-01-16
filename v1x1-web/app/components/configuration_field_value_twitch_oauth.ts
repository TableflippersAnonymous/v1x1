import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
@Component({
  selector: 'configuration-field-value-twitch-oauth',
  template: `twitch-oauth`
})
export class ConfigurationFieldValueTwitchOauthComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public configuration: Object;
}
