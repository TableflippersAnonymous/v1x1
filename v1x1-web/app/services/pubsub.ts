import {Injectable} from "@angular/core";
import {V1x1WebInfo} from "./web_info";
import websocketConnect, {Connection} from "rxjs-websockets";
import {QueueingSubject} from "queueing-subject";
import {Observable} from "rxjs";
import {V1x1PubSubFrame} from "../model/v1x1_pub_sub_frame";
import {JsonConvert} from "json2typescript";
import {V1x1PubSubHelloFrame} from "../model/v1x1_pub_sub_hello_frame";
import {V1x1PubSubErrorFrame} from "../model/v1x1_pub_sub_error_frame";
import {V1x1PubSubAuthRequestFrame} from "../model/v1x1_pub_sub_auth_request_frame";
import {V1x1PubSubAuthResponseFrame} from "../model/v1x1_pub_sub_auth_response_frame";
import {V1x1PubSubListenRequestFrame} from "../model/v1x1_pub_sub_listen_request_frame";
import {V1x1PubSubListenResponseFrame} from "../model/v1x1_pub_sub_listen_response_frame";
import {V1x1PubSubUnlistenRequestFrame} from "../model/v1x1_pub_sub_unlisten_request_frame";
import {V1x1PubSubUnlistenResponseFrame} from "../model/v1x1_pub_sub_unlisten_response_frame";
import {V1x1PubSubPublishRequestFrame} from "../model/v1x1_pub_sub_publish_request_frame";
import {V1x1PubSubPublishResponseFrame} from "../model/v1x1_pub_sub_publish_response_frame";
import {V1x1PubSubTopicMessageFrame} from "../model/v1x1_pub_sub_topic_message_frame";
import {V1x1PubSubResponseFrame} from "../model/v1x1_pub_sub_response_frame";
import {V1x1GlobalState} from "./global_state";
import {delay, filter, find, first, map, mergeAll, publishReplay, refCount, retryWhen, share} from "rxjs/operators";

@Injectable()
export class V1x1PubSub {
  private inputStream: QueueingSubject<any> = new QueueingSubject();
  public messages: Observable<string>;
  public frames: Observable<V1x1PubSubFrame>;
  public connection: Observable<Connection>;
  private observables: Map<string, Observable<Object>> = new Map<string, Observable<Object>>();
  private authObservable: Observable<V1x1PubSubAuthResponseFrame>;
  private topics: Set<string> = new Set<string>();

  constructor(private webInfo: V1x1WebInfo, private globalState: V1x1GlobalState) {}

  connect() {
    if(this.connection) {
      return;
    }
    this.connection = this.webInfo.getWebConfig().pipe(map(wc =>
      websocketConnect(wc.pubsubBase + "/pubsub", this.inputStream)
    ));
    this.messages = this.connection.pipe(map(conn =>
      conn.messages.pipe(share(), retryWhen(errors => errors.pipe(delay(1000))))
    ), mergeAll(), share());
    this.frames = this.messages.pipe(map(message => this.parseFrame(message)), share());
    this.frames.pipe(filter(frame => frame instanceof V1x1PubSubHelloFrame)).subscribe((frame: V1x1PubSubHelloFrame) => {
      this.authObservable = this.globalState.authorization.get().pipe(first(), map(authorization => this.auth(authorization)), mergeAll(), publishReplay(1), refCount());
      this.topics.forEach((topic: string) => {
        this.listen(topic).subscribe();
      });
    });
    this.authObservable = this.globalState.authorization.get().pipe(first(), map(authorization => this.auth(authorization)), mergeAll(), publishReplay(1), refCount());
  }

  public topic(topic: string): Observable<Object> {
    console.log("WS Connect: " + topic);
    if(this.observables.has(topic))
      return this.observables.get(topic);
    this.connect();
    let observable = Observable.create(observer => {
      let subscription = this.frames.pipe(filter(frame => frame instanceof V1x1PubSubTopicMessageFrame)).subscribe((frame: V1x1PubSubTopicMessageFrame) => {
        console.log("WS Frame: " + frame.payload);
        observer.next(JSON.parse(frame.payload));
      }, error => {
        console.log(error);
        observer.error(error);
      }, () => {
        console.log("WS Complete.");
        observer.complete();
      });
      let listenSub = this.listen(topic).subscribe();
      return () => {
        listenSub.unsubscribe();
        this.unlisten(topic).subscribe();
        subscription.unsubscribe();
      };
    }).pipe(share());
    this.observables.set(topic, observable);
    return observable;
  }

  private parseFrame(message: string): V1x1PubSubFrame {
    let baseFrame = JsonConvert.deserializeString(message, V1x1PubSubFrame);
    switch(baseFrame.type) {
      case "HELLO": return JsonConvert.deserializeString(message, V1x1PubSubHelloFrame);
      case "ERROR": return JsonConvert.deserializeString(message, V1x1PubSubErrorFrame);
      case "AUTH_REQUEST": return JsonConvert.deserializeString(message, V1x1PubSubAuthRequestFrame);
      case "AUTH_RESPONSE": return JsonConvert.deserializeString(message, V1x1PubSubAuthResponseFrame);
      case "LISTEN_REQUEST": return JsonConvert.deserializeString(message, V1x1PubSubListenRequestFrame);
      case "LISTEN_RESPONSE": return JsonConvert.deserializeString(message, V1x1PubSubListenResponseFrame);
      case "UNLISTEN_REQUEST": return JsonConvert.deserializeString(message, V1x1PubSubUnlistenRequestFrame);
      case "UNLISTEN_RESPONSE": return JsonConvert.deserializeString(message, V1x1PubSubUnlistenResponseFrame);
      case "PUBLISH_REQUEST": return JsonConvert.deserializeString(message, V1x1PubSubPublishRequestFrame);
      case "PUBLISH_RESPONSE": return JsonConvert.deserializeString(message, V1x1PubSubPublishResponseFrame);
      case "TOPIC_MESSAGE": return JsonConvert.deserializeString(message, V1x1PubSubTopicMessageFrame);
    }
    return baseFrame;
  }

  private auth(authorization: string): Observable<V1x1PubSubAuthResponseFrame> {
    let requestFrame = new V1x1PubSubAuthRequestFrame(authorization);
    return <Observable<V1x1PubSubAuthResponseFrame>> this.request(requestFrame);
  }

  private listen(topic: string): Observable<V1x1PubSubListenResponseFrame> {
    let requestFrame = new V1x1PubSubListenRequestFrame(topic);
    return <Observable<V1x1PubSubListenResponseFrame>> this.authObservable.pipe(first(), map(() => this.request(requestFrame).pipe(map(r => {
      this.topics.add(topic);
      return r;
    }))), mergeAll());
  }

  private unlisten(topic: string): Observable<V1x1PubSubUnlistenResponseFrame> {
    let requestFrame = new V1x1PubSubUnlistenRequestFrame(topic);
    this.topics.delete(topic);
    return <Observable<V1x1PubSubUnlistenResponseFrame>> this.request(requestFrame);
  }

  private publish(topic: string, payload: string): Observable<V1x1PubSubPublishResponseFrame> {
    let requestFrame = new V1x1PubSubPublishRequestFrame(topic, payload);
    return <Observable<V1x1PubSubPublishResponseFrame>> this.request(requestFrame);
  }

  private request(request: V1x1PubSubFrame): Observable<V1x1PubSubResponseFrame> {
    this.send(request);
    // Need to handle ErrorFrame separately.
    return <Observable<V1x1PubSubResponseFrame>> this.frames.pipe(find(frame => frame instanceof V1x1PubSubResponseFrame && frame.responseTo == request.id), first());
  }

  private send(request: V1x1PubSubFrame) {
    console.log("WS Send: " + JSON.stringify(request));
    this.inputStream.next(JSON.stringify(request));
  }
}
