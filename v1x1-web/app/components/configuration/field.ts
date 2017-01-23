import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField, ConfigType, Permission} from "../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "./configurable";
@Component({
  selector: 'configuration-field',
  template: `
    <div class="form-group" *ngIf="field.tenantPermission !== permissions.NONE">
      <label *ngIf="field.configType !== configTypes.MASTER_ENABLE && field.configType !== configTypes.BOOLEAN">{{field.displayName}}</label>
      <div class="config-group" style="padding-left: 10px;" [class.config-group-dirty]="configDirty()">
        <configuration-field-value [field]="field" [complexFields]="complexFields" [originalConfiguration]="originalConfiguration" [(configuration)]="configuration"></configuration-field-value>
        <small class="form-text text-muted">{{field.description}}</small>
        <small class="form-text text-muted config-dirty-text" *ngIf="configDirty() && originalConfigPrimitive()">Was: {{originalConfiguration}}</small>
      </div>
    </div>
  `
})
export class ConfigurationFieldComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
  public configTypes = ConfigType;
  public permissions = Permission;

  originalConfigPrimitive() {
    return typeof this.originalConfiguration !== 'object' && typeof this.originalConfiguration !== 'array';
  }
}
