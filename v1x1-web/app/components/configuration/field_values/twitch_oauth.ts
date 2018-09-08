import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/api/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-twitch-oauth',
  template: `
    <span>
      <mat-form-field class="input-group">
        <input matInput type="text" [ngModel]="configuration" (blur)="configuration = $event.target.value" class="form-control" placeholder="{{field.displayName}}">
      </mat-form-field>
      <span class="input-group-append" *ngIf="configDirty() && originalValid()">
        <button mat-button color="accent" (click)="abandonChanges()"><i class="far fa-undo"></i></button>
      </span>
    </span>
  `
})
export class ConfigurationFieldValueTwitchOauthComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;

  originalValid() {
    return typeof this.originalConfiguration === 'string';
  }
}
