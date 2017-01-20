import {Component, Input, Output} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "./configurable";
@Component({
  selector: 'configuration-field-value-string-map',
  template: `
    <div class="container-fluid">
      <div class="row" *ngFor="let elem of object.keys(configuration || {})" >
        <div class="col-2">
          <div class="input-group">
            <input type="text" class="form-control" [ngModel]="elem" (ngModelChange)="changeKey(elem, $event);">
            <span class="input-group-btn">
              <button class="btn btn-danger" (click)="delKey(elem)">&times;</button>
            </span>
          </div>
        </div>
        <div class="col" style="border-left: 2px solid rgb(238, 238, 238); margin-bottom: 1rem;">
          <configuration-field-value-string [field]="field" [(configuration)]="configuration[elem]"></configuration-field-value-string>
        </div>
      </div>
      <div class="row">
        <div class="col-2">
          <div class="input-group">
            <input type="text" class="form-control" [(ngModel)]="nextValue" placeholder="Add an entry ...">
            <span class="input-group-btn">
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
    if(this.configuration === undefined)
      this.configuration = {};
    this.configuration[value] = "";
    this.nextValue = "";
  }

  public changeKey(oldKey: string, newKey: string) {
    this.configuration[newKey] = this.configuration[oldKey];
    delete this.configuration[oldKey];
  }

  public delKey(key: string) {
    delete this.configuration[key];
  }

  public object = Object;
}
