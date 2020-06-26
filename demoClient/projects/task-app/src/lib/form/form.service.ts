import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { IForRootConf } from '../IForRootConf';
import { IP_CONFIG } from '../tokens';

@Injectable({
  providedIn: 'root'
})
export class FormService {

  url = "";
  resp: any
  constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) private _config: IForRootConf) {
    this.url = _config.apiPath;
  }

  public getTaskForm(taskId): Observable<any> {
    return this.httpclient.get(this.url + '/app/rest/task-forms/' + taskId);
  }

  public completeTaskForm(taskId, data): Observable<any> {
    return this.httpclient.post(this.url + '/app/rest/task-forms/' + taskId, data);
  }

  public saveTaskForm(taskId, data): Observable<any> {
    return this.httpclient.post(this.url + '/app/rest/task-forms/' + taskId + '/save-form', data);
  }

  public getStartForm(processDefinitionId): Observable<any> {
    return this.httpclient.get(this.url + '/app/rest/process-definitions/' + processDefinitionId + "/start-form");
  }

  public completeStartForm(data): Observable<any> {
    return this.httpclient.post(this.url + '/app/rest/process-instances/', data);
  }

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