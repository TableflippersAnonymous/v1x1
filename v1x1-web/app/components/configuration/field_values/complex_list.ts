import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";

@Component({
  selector: 'configuration-field-value-complex-list',
  template: `
    <div class="card-container">
      <mat-card>
        <mat-card-header [class.config-group-dirty]="configDirty()">
          <mat-card-title><h2>{{field.displayName}}</h2></mat-card-title>
          <mat-card-subtitle *ngIf="field.description !== '<no description>'">{{field.description}}</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <div class="card-container" *ngFor="let elem of configuration; let i = index">
            <mat-card>
              <mat-card-header [class.config-group-dirty]="configIdxDirty(i)">
                <mat-card-title><h3>Entry</h3></mat-card-title>
              </mat-card-header>
              <mat-card-content>
                <configuration-field-value-complex [field]="field"
                                                   [complexFields]="complexFields"
                                                   [originalConfiguration]="originalConfiguration[i]"
                                                   [configuration]="configuration[i]"
                                                   (configurationChange)="configuration[i] = $event; configChanged()"
                                                   [activeTenant]="activeTenant" [channelGroup]="channelGroup"></configuration-field-value-complex>
              </mat-card-content>
              <mat-card-actions>
                <button mat-raised-button color="accent" (click)="delIdx(i);">&times;</button>
              </mat-card-actions>
            </mat-card>
          </div>
        </mat-card-content>
        <mat-card-actions>
          <button mat-raised-button color="accent" (click)="addList()">+</button>
        </mat-card-actions>
      </mat-card>
    </div>
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
