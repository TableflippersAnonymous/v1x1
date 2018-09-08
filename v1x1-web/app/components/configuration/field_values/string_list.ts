import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/api/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-string-list',
  template: `
    <div *ngFor="let elem of configuration; let i = index" style="padding-left: 10px; margin-bottom: 1rem;" class="config-group" [class.config-group-dirty]="configIdxDirty(i)">
      <div class="input-group">
        <configuration-field-value-string [field]="field" [originalConfiguration]="originalConfiguration[i]" [configuration]="configuration[i]" (configurationChange)="configuration[i] = $event; configChanged()" [activeTenant]="activeTenant" [channelGroup]="channelGroup"></configuration-field-value-string>
        <span class="input-group-append">
          <button mat-button color="accent" (click)="delIdx(i);">&times;</button>
        </span>
      </div>
    </div>
    <button mat-raised-button color="accent" (click)="addList()">+</button>
`
})
export class ConfigurationFieldValueStringListComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;

  public addList() {
    if(!(this.configuration instanceof Array))
      this.configuration = [];
    this.configuration.push("");
    this.configChanged();
  }

  public delIdx(index: number) {
    this.configuration.splice(index, 1);
    this.configChanged();
  }
}
