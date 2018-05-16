import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {V1x1WebConfig} from "../model/v1x1_web_config";
import {Observable} from "rxjs";
import {JsonConvert} from "json2typescript";
import {map, publishReplay, refCount} from "rxjs/operators";

@Injectable()
export class V1x1WebInfo {
  private webConfig: Observable<V1x1WebConfig>;

  constructor(private http: HttpClient) {}

  fetchWebConfig(): Observable<V1x1WebConfig> {
    return this.http.get("/api/v1/platform/web/config")
      .pipe(map((r) => JsonConvert.deserializeObject(r, V1x1WebConfig)));
  }

  getWebConfig(): Observable<V1x1WebConfig> {
    if(!this.webConfig)
      this.webConfig = this.fetchWebConfig().pipe(publishReplay(1), refCount());
    return this.webConfig;
  }
}
