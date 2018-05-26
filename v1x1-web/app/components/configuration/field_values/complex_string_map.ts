import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-complex-string-map',
  template: `
    <div class="card-container">
      <mat-card>
        <mat-card-header [class.config-group-dirty]="configDirty()">
          <mat-card-title><h2>{{field.displayName}}</h2></mat-card-title>
          <mat-card-subtitle *ngIf="field.description !== '<no description>'">{{field.description}}</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <div class="card-container" *ngFor="let elem of object.keys(configuration || {})">
            <mat-card>
              <mat-card-header [class.config-group-dirty]="configFieldDirty(elem)">
                <mat-card-title style="margin-top: 16px;">
                  <mat-form-field class="input-group">
                    <input matInput type="text" [ngModel]="elem" (blur)="changeKey(elem, $event.target.value);">
                  </mat-form-field>
                </mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <configuration-field-value-complex [field]="field"
                                                   [complexFields]="complexFields"
                                                   [originalConfiguration]="originalConfiguration[elem]"
                                                   [configuration]="configuration[elem]"
                                                   (configurationChange)="setConfigField(elem, $event)"
                                                   [activeTenant]="activeTenant" [channelGroup]="channelGroup"></configuration-field-value-complex>
              </mat-card-content>
              <mat-card-actions>
                <button mat-raised-button color="accent" (click)="delKey(elem)">&times;</button>
              </mat-card-actions>
            </mat-card>
          </div>
        </mat-card-content>
        <mat-card-actions>
          <mat-form-field class="input-group">
            <input matInput type="text" [(ngModel)]="nextValue" placeholder="Add an entry ...">
          </mat-form-field>
          <span class="input-group-append">
            <button mat-raised-button color="accent" (click)="addValue(nextValue)">+</button>
          </span>
        </mat-card-actions>
      </mat-card>
    </div>
  `
})
export class ConfigurationFieldValueComplexStringMapComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;
  @Input() public complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};

  public nextValue: string = "";

  public addValue(value: string) {
    if(this.configuration instanceof Object)
      while(this.configuration.hasOwnProperty(value))
        value = value + "_";
    this.setConfigField(value, {});
    this.nextValue = "";
  }

  public changeKey(oldKey: string, newKey: string) {
    if(oldKey === newKey)
      return;
    if(this.configuration instanceof Object)
      while(this.configuration.hasOwnProperty(newKey))
        newKey = newKey + "_";
    this.renameConfigField(oldKey, newKey);
  }

  public delKey(key: string) {
    this.deleteConfigField(key);
  }

  public object = Object;
}
