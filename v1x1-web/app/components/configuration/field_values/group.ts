import {ConfigurableComponent} from "../configurable";
import {Component, Input, OnInit} from "@angular/core";
import {Observable} from "rxjs/index";
import {V1x1ConfigurationDefinitionField} from "../../../model/api/v1x1_configuration_definition_field";
import {V1x1GlobalState} from "../../../services/global_state";
import {map} from "rxjs/operators";
import {TenantGroup} from "../../../model/state/tenant_group";
import {Tenant} from "../../../model/state/tenant";

@Component({
  selector: 'configuration-field-value-group',
  template: `
    <mat-form-field>
      <mat-select placeholder="Group" [(value)]="configuration">
        <mat-option *ngFor="let group of groups | async" [value]="group.groupId">
          {{group.name}}
        </mat-option>
      </mat-select>
    </mat-form-field>
    <span class="input-group-append" *ngIf="configDirty()">
      <button mat-button color="accent" (click)="abandonChanges()"><i class="far fa-undo"></i></button>
    </span>
`
})
export class ConfigurationFieldValueGroupComponent extends ConfigurableComponent implements OnInit {
  @Input() public field: V1x1ConfigurationDefinitionField;
  groups: Observable<Iterable<TenantGroup>> = null;

  constructor(private globalState: V1x1GlobalState) {
    super();
  }

  ngOnInit(): void {
    this.groups = this.globalState.webapp.currentTenant.pipe(map((activeTenant: Tenant) => activeTenant.groups.values()));
  }
}
