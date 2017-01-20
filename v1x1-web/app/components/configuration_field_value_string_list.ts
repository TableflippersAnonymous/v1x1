import {Component, Input, Output} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "./configurable";
@Component({
  selector: 'configuration-field-value-string-list',
  template: `
    <div *ngFor="let elem of configuration; let i = index" style="border-left: 2px solid rgb(238, 238, 238); margin-bottom: 1rem;">
      <div class="input-group">
        <configuration-field-value-string [field]="field" [(configuration)]="configuration[i]"></configuration-field-value-string>
        <span class="input-group-btn">
          <button class="btn btn-danger" (click)="delIdx(i);">&times;</button>
        </span>
      </div>
    </div>
    <button class="btn btn-warning" (click)="addList()">+</button>
`
})
export class ConfigurationFieldValueStringListComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;

  public addList() {
    if(!(this.configuration instanceof Array))
      this.configuration = [];
    this.configuration.push({});
  }

  public delIdx(index: number) {
    this.configuration.splice(index, 1);
  }
}
