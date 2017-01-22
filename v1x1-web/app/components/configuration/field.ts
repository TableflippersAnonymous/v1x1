import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField, ConfigType, Permission} from "../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "./configurable";
@Component({
  selector: 'configuration-field',
  template: `
    <div class="form-group" *ngIf="field.tenantPermission !== permissions.NONE">
      <label *ngIf="field.configType !== configTypes.MASTER_ENABLE && field.configType !== configTypes.BOOLEAN">{{field.displayName}}</label>
      <div style="border-left: 2px solid rgb(238, 238, 238); padding-left: 10px;">
        <configuration-field-value [field]="field" [complexFields]="complexFields" [(configuration)]="configuration"></configuration-field-value>
        <small class="form-text text-muted">{{field.description}}</small>
      </div>
    </div>
  `
})
export class ConfigurationFieldComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
  public configTypes = ConfigType;
  public permissions = Permission;
}
