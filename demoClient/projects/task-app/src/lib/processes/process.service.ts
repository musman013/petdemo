import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { IForRootConf } from '../IForRootConf';
import { IP_CONFIG } from '../tokens';

@Injectable({
  providedIn: 'root'
})
// export class TriggerService extends GenericApiService<ITrigger> {
export class ProcessService {

  url = "";
  resp: any
  constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) private _config: IForRootConf) {
    this.url = _config.apiPath;
  }

  public get(processId): Observable<any> {
    return this.httpclient.get(this.url + '/app/rest/process-instances/' + processId);
  }

  public queryProcesses(data): Observable<any> {
    return this.httpclient.post(this.url + '/app/rest/query/process-instances', data);
  };

  createProcess(processData) {
    return this.httpclient.post(this.url + '/app/rest/process-instances', processData);
  };

  deleteProcess(processInstanceId) {
    return this.httpclient.delete(this.url + '/app/rest/process-instances/' + processInstanceId);
  };

  getProcessDefinitions(appDefinitionKey): Observable<any> {
    let def_url = this.url + '/app/rest/process-definitions?latest=true'
    if (appDefinitionKey) {
      def_url += '&appDefinitionKey=' + appDefinitionKey;
    }
    return this.httpclient.get(def_url);
  };

  protected handleError(err: HttpErrorResponse) {

    let errorMessage = '';
    if (err.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errorMessage = `An error occurred: ${err.error.message}`;
    } else {

      errorMessage = `Server returned code: ${err.status}, error message is: ${err.message}`;
    }
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}