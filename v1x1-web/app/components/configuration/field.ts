import {Component, Input} from "@angular/core";
import {
  ConfigType,
  Permission,
  V1x1ConfigurationDefinitionField
} from "../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "./configurable";

@Component({
  selector: 'configuration-field',
  template: `
    <div class="form-group" *ngIf="field.tenantPermission !== permissions.NONE">
      <label *ngIf="shouldShowLabel(field.configType)">{{field.displayName}}</label>
      <div class="config-group" style="padding-top: 1rem; padding-left: 30px;" [class.config-group-dirty]="configDirty()" *ngIf="!complex(field.configType)">
        <configuration-field-value [field]="field" [complexFields]="complexFields"
                                   [activeTenant]="activeTenant" [activeChannelGroup]="activeChannelGroup" [activeChannel]="activeChannel"
                                   [originalConfiguration]="originalConfiguration" [(configuration)]="configuration"></configuration-field-value>
        <small class="form-text text-muted" *ngIf="field.description !== '<no description>' && shouldShowDescription(field.configType)">{{field.description}}</small>
        <small class="form-text text-muted config-dirty-text" *ngIf="configDirty() && originalConfigPrimitive()">Was: {{originalConfiguration}}</small>
      </div>
      <configuration-field-value *ngIf="complex(field.configType)" [field]="field" [complexFields]="complexFields"
                                 [activeTenant]="activeTenant" [activeChannelGroup]="activeChannelGroup" [activeChannel]="activeChannel"
                                 [originalConfiguration]="originalConfiguration" [(configuration)]="configuration"></configuration-field-value>
    </div>
  `
})
export class ConfigurationFieldComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
  public permissions = Permission;

  originalConfigPrimitive() {
    return typeof this.originalConfiguration !== 'object';
  }

  complex(type: ConfigType): boolean {
    return type === ConfigType.COMPLEX_STRING_MAP ||
      type === ConfigType.COMPLEX_LIST ||
      type === ConfigType.COMPLEX;
  }

  shouldShowLabel(type: ConfigType): boolean {
    return !this.complex(type) &&
      type !== ConfigType.MASTER_ENABLE &&
      type !== ConfigType.BOOLEAN &&
      type !== ConfigType.STRING &&
      type !== ConfigType.INTEGER &&
      type !== ConfigType.PERMISSION;
  }

  shouldShowDescription(type: ConfigType): boolean {
    return type !== ConfigType.OAUTH;
  }
}
