import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-string-map',
  template: `
    <div class="container-fluid">
      <div class="row" *ngFor="let elem of object.keys(configuration || {})" >
        <div class="col-2">
          <div class="input-group">
            <input type="text" class="form-control" [ngModel]="elem" (blur)="changeKey(elem, $event.target.value);">
            <span class="input-group-append">
              <button class="btn btn-danger" (click)="delKey(elem)">&times;</button>
            </span>
          </div>
        </div>
        <div class="col config-group" style="margin-bottom: 1rem;" [class.config-group-dirty]="configFieldDirty(elem)">
          <configuration-field-value-string [field]="field" [originalConfiguration]="originalConfiguration[elem]" [configuration]="configuration[elem]" (configurationChange)="setConfigField(elem, $event)" [activeTenant]="activeTenant"></configuration-field-value-string>
        </div>
      </div>
      <div class="row">
        <div class="col-2">
          <div class="input-group">
            <input type="text" class="form-control" [(ngModel)]="nextValue" placeholder="Add an entry ...">
            <span class="input-group-append">
              <button class="btn btn-warning" (click)="addValue(nextValue)">+</button>
            </span>
          </div>
        </div>
      </div>
    </div>
`
})
export class ConfigurationFieldValueStringMapComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  public nextValue: string;

  public addValue(value: string) {
    this.setConfigField(value, "");
    this.nextValue = "";
  }

  public changeKey(oldKey: string, newKey: string) {
    if(oldKey === newKey)
      return;
    this.setConfigField(newKey, this.configuration[oldKey]);
    this.deleteConfigField(oldKey);
  }

  public delKey(key: string) {
    this.deleteConfigField(key);
  }

  public object = Object;
}
