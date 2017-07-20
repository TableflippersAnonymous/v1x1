import {Component} from "@angular/core";
import {V1x1PubSub} from "../../services/pubsub";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1ChatMessage} from "../../model/v1x1_chat_message";
import {JsonConvert} from "json2typescript";
@Component({
  selector: 'dashboard-page',
  template: `Dashboard goes here
    <div *ngFor="let line of lines">
      <p>{{line}}</p>
    </div>
  `
})
export class DashboardPageComponent {
  public lines: string[] = [];

  constructor(private pubsub: V1x1PubSub, private api: V1x1Api, private apiCache: V1x1ApiCache, private globalState: V1x1GlobalState) {
    this.globalState.activeTenant.get().subscribe(tenant => {
      this.pubsub.topic("topic:" + tenant.id + ":api:chat").subscribe(message => {
        let chatMessage: V1x1ChatMessage = JsonConvert.deserializeObject(message, V1x1ChatMessage);
        this.lines.push("<" + chatMessage.user.displayName + "> " + chatMessage.text);
      })
    });
  }


}
