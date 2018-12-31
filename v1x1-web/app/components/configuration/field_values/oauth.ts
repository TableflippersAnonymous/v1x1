import {Component, Input} from "@angular/core";
import {V1x1ConfigurationDefinitionField} from "../../../model/v1x1_configuration_definition_field";
import {ConfigurableComponent} from "../configurable";
import {V1x1Api} from "../../../services/api";
import {V1x1WebInfo} from "../../../services/web_info";
import {take} from "rxjs/operators";

@Component({
  selector: 'configuration-field-value-oauth',
  template: `
    <span>
      <button mat-raised-button class="btn-{{field.description}}" (click)="doLogin()"><i class="fab fa-{{getFaIcon(field.description)}}"></i> Sign In</button>
    </span>
  `
})
export class ConfigurationFieldValueOauthComponent extends ConfigurableComponent {
  @Input() public field: V1x1ConfigurationDefinitionField;

  constructor(private api: V1x1Api, private webInfo: V1x1WebInfo) {
    super();
  }

  public doLogin() {
    this.webInfo.getWebConfig().pipe(take(1)).subscribe(wc => {
      this.api.getSpecializedState(this.field.description, this.activeTenant.id,
        this.activeChannelGroup == null ? "" : this.activeChannelGroup.platform,
        this.activeChannelGroup == null ? "" : this.activeChannelGroup.id,
        this.activeChannel == null ? "" : this.activeChannel.id).pipe(take(1)).subscribe(state => {
          window.location.href = wc.configOauthUrls[this.field.description] + state.state;
      });
    });
  }

  public getFaIcon(description: string): string {
    switch(description) {
      case 'TMI': return 'twitch';
      case 'SPOTIFY': return 'spotify';
    }
    return 'unknown';
  }
}
