import {Component, Input, Output, EventEmitter} from "@angular/core";
import {V1x1ConfigurationDefinitionField, ConfigType} from "../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "./configurable";

@Component({
  selector: 'configuration-field-value',
  template: `
    <configuration-field-value-integer *ngIf="field.configType === configTypes.INTEGER" [field]="field" [(configuration)]="configuration"></configuration-field-value-integer>
    <configuration-field-value-string *ngIf="field.configType === configTypes.STRING" [field]="field" [(configuration)]="configuration"></configuration-field-value-string>
    <configuration-field-value-credential *ngIf="field.configType === configTypes.CREDENTIAL" [field]="field" [(configuration)]="configuration"></configuration-field-value-credential>
    <configuration-field-value-twitch-oauth *ngIf="field.configType === configTypes.TWITCH_OAUTH" [field]="field" [(configuration)]="configuration"></configuration-field-value-twitch-oauth>
    <configuration-field-value-master-enable *ngIf="field.configType === configTypes.MASTER_ENABLE" [field]="field" [(configuration)]="configuration"></configuration-field-value-master-enable>
    <configuration-field-value-boolean *ngIf="field.configType === configTypes.BOOLEAN" [field]="field" [(configuration)]="configuration"></configuration-field-value-boolean>
    <configuration-field-value-bot-name *ngIf="field.configType === configTypes.BOT_NAME" [field]="field" [(configuration)]="configuration"></configuration-field-value-bot-name>
    <configuration-field-value-string-list *ngIf="field.configType === configTypes.STRING_LIST" [field]="field" [(configuration)]="configuration"></configuration-field-value-string-list>
    <configuration-field-value-string-map *ngIf="field.configType === configTypes.STRING_MAP" [field]="field" [(configuration)]="configuration"></configuration-field-value-string-map>
    <configuration-field-value-complex *ngIf="field.configType === configTypes.COMPLEX" [field]="field" [complexFields]="complexFields" [(configuration)]="configuration"></configuration-field-value-complex>
    <configuration-field-value-complex-list *ngIf="field.configType === configTypes.COMPLEX_LIST" [field]="field" [complexFields]="complexFields" [(configuration)]="configuration"></configuration-field-value-complex-list>
    <configuration-field-value-complex-string-map *ngIf="field.configType === configTypes.COMPLEX_STRING_MAP" [field]="field" [complexFields]="complexFields" [(configuration)]="configuration"></configuration-field-value-complex-string-map>
  `
})
export class ConfigurationFieldValueComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
  public configTypes = ConfigType;
}
