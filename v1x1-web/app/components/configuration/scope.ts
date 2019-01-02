import {Component, EventEmitter, Input, Output} from "@angular/core";
import {V1x1Module} from "../../model/v1x1_module";
import {V1x1ConfigurationDefinition} from "../../model/v1x1_configuration_definition";
import {ConfigurableComponent} from "./configurable";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1Configuration} from "../../model/v1x1_configuration";
import {V1x1ConfigurationDefinitionField} from "../../model/v1x1_configuration_definition_field";
import {JsonConvert} from "json2typescript";

@Component({
  selector: 'configuration-scope',
  template: `
    <div class="card-container">
      <form>
        <mat-card>
          <mat-card-header>
            <mat-card-title><h1>{{v1x1Module.displayName}}</h1></mat-card-title>
            <mat-card-subtitle>{{v1x1Module.description}}</mat-card-subtitle>
          </mat-card-header>
          <mat-card-content>
            <configuration-field *ngIf="scope !== 'tenant'"
                                 [field]="overrideField" [complexFields]="{}"
                                 [originalConfiguration]="originalEnabled"
                                 [activeTenant]="activeTenant" [activeChannelGroup]="activeChannelGroup" [activeChannel]="activeChannel"
                                 [configuration]="enabled" (configurationChange)="setEnabled($event)"></configuration-field>
            <configuration-field *ngFor="let field of configurationDefinition.fields"
                                 [field]="field" [complexFields]="configurationDefinition.complexFields"
                                 [originalConfiguration]="originalConfiguration[field.jsonField]"
                                 [activeTenant]="activeTenant" [activeChannelGroup]="activeChannelGroup" [activeChannel]="activeChannel"
                                 [configuration]="configuration[field.jsonField]" (configurationChange)="setConfigField(field.jsonField, $event)"></configuration-field>
          </mat-card-content>
          <mat-card-actions>
            <button mat-raised-button color="primary" *ngIf="configDirty()" (click)="saveChanges()">Save Changes</button>
            <button mat-raised-button color="accent" *ngIf="configDirty()" (click)="abandonChanges()">Abandon Changes</button>
          </mat-card-actions>
        </mat-card>
      </form>
    </div>
  `
})
export class ConfigurationScopeComponent extends ConfigurableComponent {
  @Input() public v1x1Module: V1x1Module;
  @Input() public configurationDefinition: V1x1ConfigurationDefinition;
  @Input() public scope: string;
  @Input() public enabled: boolean;
  @Output() public enabledChange = new EventEmitter();
  @Input() public originalEnabled: boolean;
  @Output() public originalEnabledChange = new EventEmitter();
  public json = JSON;
  public overrideField = JsonConvert.deserializeObject({
    display_name: "Override?",
    description: "This controls whether this overrides the settings in Everything.",
    default_value: "null",
    config_type: "MASTER_ENABLE",
    requires: [],
    tenant_permission: "READ_WRITE",
    json_field: "enabled",
    complex_type: null
  }, V1x1ConfigurationDefinitionField);

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api) {
    super();
  }

  saveChanges() {
    if(this.scope === 'tenant')
      this.api.putTenantConfiguration(this.activeTenant.id, this.v1x1Module.name, new V1x1Configuration(true, this.configuration))
        .subscribe(v1x1Config => {
          this.originalConfigurationValue = JSON.parse(JSON.stringify(v1x1Config.configuration));
          this.originalConfigurationChange.emit(this.originalConfigurationValue);
          this.configDirtyChange.emit(this.configDirty());
        });
    else if(this.scope === 'channelGroup')
      this.api.putChannelGroupConfiguration(this.activeTenant.id, this.v1x1Module.name, this.activeChannelGroup.platform, this.activeChannelGroup.id, new V1x1Configuration(this.enabled, this.configuration))
        .subscribe(v1x1Config => {
          this.originalEnabled = v1x1Config.enabled;
          this.originalEnabledChange.emit(this.originalEnabled);
          this.originalConfigurationValue = JSON.parse(JSON.stringify(v1x1Config.configuration));
          this.originalConfigurationChange.emit(this.originalConfigurationValue);
          this.configDirtyChange.emit(this.configDirty());
        });
    else if(this.scope === 'channel')
      this.api.putChannelConfiguration(this.activeTenant.id, this.v1x1Module.name, this.activeChannelGroup.platform, this.activeChannelGroup.id, this.activeChannel.id, new V1x1Configuration(this.enabled, this.configuration))
        .subscribe(v1x1Config => {
          this.originalEnabled = v1x1Config.enabled;
          this.originalEnabledChange.emit(this.originalEnabled);
          this.originalConfigurationValue = JSON.parse(JSON.stringify(v1x1Config.configuration));
          this.originalConfigurationChange.emit(this.originalConfigurationValue);
          this.configDirtyChange.emit(this.configDirty());
        });
    else
      this.acceptChanges();
  }

  setEnabled(enabled: boolean) {
    this.enabled = enabled;
    this.enabledChange.emit(enabled);
    this.configChanged();
  }

  configDirty(): boolean {
    return super.configDirty() || this.enabled !== this.originalEnabled;
  }

  abandonChanges(): void {
    this.enabled = this.originalEnabled;
    this.enabledChange.emit(this.enabled);
    super.abandonChanges();
  }
}
