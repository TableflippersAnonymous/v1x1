import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
@Component({
  selector: 'configuration-field-value-complex-string-map',
  template: `
    <div class="container-fluid">
      <div class="row">
        <div class="col-2">
          <input type="text" class="form-control">
        </div>
        <div class="col">
          <configuration-field-value-complex [field]="field" [complexFields]="complexFields" [configuration]="configuration"></configuration-field-value-complex>
        </div>
      </div>
      <button class="btn btn-warning">+</button>
    </div>
  `
})
export class ConfigurationFieldValueComplexStringMapComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};
  @Input() public configuration: Object;
}
