import {Component, Input} from "@angular/core";
import {V1x1Module} from "../../model/v1x1_module";
import {V1x1ConfigurationDefinition} from "../../model/v1x1_configuration_definition";
import {ConfigurableComponent} from "./configurable";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1Configuration} from "../../model/v1x1_configuration";
import {V1x1Tenant} from "../../model/v1x1_tenant";
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
    </div>
  `
})
export class ConfigurationScopeComponent extends ConfigurableComponent {
  @Input() public v1x1Module: V1x1Module;
  @Input() public configurationDefinition: V1x1ConfigurationDefinition;
  @Input() public scope: string;
  @Input() public activeTenant: V1x1Tenant;
  public json = JSON;

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api) {
    super();
  }

  saveChanges() {
    if(this.scope === 'tenant')
      this.api.putTenantConfiguration(this.activeTenant.id, this.v1x1Module.name, new V1x1Configuration(this.configuration))
        .subscribe(v1x1Config => {
          this.originalConfigurationValue = JSON.parse(JSON.stringify(v1x1Config.configuration));
          this.originalConfigurationChange.emit(this.originalConfigurationValue);
          this.configDirtyChange.emit(this.configDirty());
        });
    else
      this.acceptChanges();
  }
}
