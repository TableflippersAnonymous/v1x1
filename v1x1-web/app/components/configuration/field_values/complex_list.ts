import {Component, Input, Output} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
@Component({
  selector: 'configuration-field-value-complex-list',
  template: `
    <div *ngFor="let elem of configuration; let i = index" style="border-left: 2px solid rgb(238, 238, 238); margin-bottom: 1rem;">
      <button class="btn btn-sm btn-danger float-left" style="margin-left: 5px; margin-right: 5px; z-index: 10; position: relative;" (click)="delIdx(i);">&times;</button>
      <configuration-field-value-complex [field]="field" [complexFields]="complexFields" [originalConfiguration]="originalConfiguration[i]" [configuration]="configuration[i]" (configurationChange)="configuration[i] = $event; configChanged()"></configuration-field-value-complex>
    </div>
    <button class="btn btn-warning" (click)="addList()">+</button>
  `
})
export class ConfigurationFieldValueComplexListComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};

  public addList() {
    if(!(this.configuration instanceof Array))
      this.configuration = [];
    this.configuration.push({});
    this.configChanged();
  }

  public delIdx(index: number) {
    this.configuration.splice(index, 1);
    this.configChanged();
  }
}
