import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-file',
  template: `
    <span>
      <button mat-raised-button color="primary" (click)="fileInput.click()">Choose File</button>
      <input hidden type="file" #fileInput (change)="fileChanged($event)">
      <span class="input-group-append" *ngIf="configDirty() && originalValid()">
        <button mat-button color="accent" (click)="abandonChanges()"><i class="far fa-undo"></i></button>
      </span>
    </span>
  `
})
export class ConfigurationFieldValueFileComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;

  originalValid() {
    return typeof this.originalConfiguration === 'string';
  }

  fileChanged(evt) {
    let fileReader = new FileReader();
    fileReader.onload = (e) => {
      let dataUrl = e.target.result;
      this.configuration = dataUrl.substr(dataUrl.indexOf(',') + 1);
    }
    fileReader.readAsArrayBuffer(evt.target.files[0]);
  }
}
