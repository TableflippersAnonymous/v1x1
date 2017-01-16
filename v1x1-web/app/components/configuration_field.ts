import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField, ConfigType} from "../model/v1x1_configuration_definition_field";
@Component({
  selector: 'configuration-field',
  template: `
    <div class="form-group">
      <label *ngIf="field.configType !== configTypes.MASTER_ENABLE && field.configType !== configTypes.BOOLEAN">{{field.displayName}}</label>
      <configuration-field-value [field]="field" [complexFields]="complexFields" [configuration]="configuration"></configuration-field-value>
      <small class="form-text text-muted">{{field.description}}</small>
    </div>
  `
})
export class ConfigurationFieldComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
  @Input() public configuration: Object;
  public configTypes = ConfigType;
}
