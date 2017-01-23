import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
@Component({
  selector: 'configuration-field-value-string',
  template: `
    <div class="input-group">
      <input type="text" [ngModel]="configuration" (blur)="configuration = $event.target.value" class="form-control" placeholder="{{field.defaultValue}}">
      <span class="input-group-btn" *ngIf="configDirty() && originalValid()">
        <button class="btn btn-secondary" (click)="abandonChanges()"><i class="fa fa-undo"></i></button>
      </span>
    </div>
  `
})
export class ConfigurationFieldValueStringComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;

  originalValid() {
    return typeof this.originalConfiguration === 'string';
  }
}
