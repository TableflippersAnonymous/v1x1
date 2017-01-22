import {Component, Input} from "@angular/core";
import {V1x1Module} from "../../model/v1x1_module";
import {V1x1ConfigurationDefinition} from "../../model/v1x1_configuration_definition";
import {ConfigurableComponent} from "./configurable";
@Component({
  selector: 'configuration-scope',
  template: `
    <div class="container-fluid" style="margin-top: 1rem;">
      <form>
        <configuration-field *ngFor="let field of configurationDefinition.fields"
                             [field]="field" [complexFields]="configurationDefinition.complexFields"
                             [originalConfiguration]="originalConfiguration[field.jsonField]"
                             [configuration]="configuration[field.jsonField]" (configurationChange)="setConfigField(field.jsonField, $event)"></configuration-field>
        <button class="btn btn-primary" *ngIf="configDirty()" (click)="saveChanges()">Save Changes</button>
        <button class="btn btn-secondary" *ngIf="configDirty()" (click)="abandonChanges()">Abandon Changes</button>
      </form>
      <div>
        {{json.stringify(configuration)}}
      </div>
    </div>
  `
})
export class ConfigurationScopeComponent extends ConfigurableComponent {
  @Input() public v1x1Module: V1x1Module;
  @Input() public configurationDefinition: V1x1ConfigurationDefinition;
  @Input() public scope: string;
  @Input() public id: string;
  public json = JSON;

  saveChanges() {
    this.acceptChanges();
    this.v1x1Module.configurationSet[this.scope].originalConfiguration = JSON.parse(JSON.stringify(this.v1x1Module.configurationSet[this.scope].configuration));
  }
}
