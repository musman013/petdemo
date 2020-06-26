import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { IForRootConf } from '../IForRootConf';
import { IP_CONFIG } from '../tokens';

@Injectable({
  providedIn: 'root'
})
export class RuntimeAppDefinitionService {
  appDefinitionKey: any;

  url = "";
  resp: any
  constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) private _config: IForRootConf) {
    this.url = _config.apiPath;
  }

  getApplications(): Observable<any> {
    return this.httpclient.get(this.url + '/app/rest/runtime/app-definitions');
  };


}
