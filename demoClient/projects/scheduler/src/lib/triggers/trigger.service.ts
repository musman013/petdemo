import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { ITrigger, Trigger } from './trigger';

import { IP_CONFIG } from '../tokens';
import { IForRootConf } from '../IForRootConf';
import { ISearchField, ServiceUtils } from 'projects/fast-code-core/src/public_api';;

@Injectable({
  providedIn: 'root'
})
// export class TriggerService extends GenericApiService<ITrigger> {
export class TriggerService {
  //environment.apiUrl;//'https://jsonplaceholder.typicode.com/users';
  baseUrl = "";
  
  resp: any
  constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) private _config: IForRootConf) {
    this.baseUrl = _config.apiPath + "/triggers";
  }

  public getAll(searchFields: ISearchField[], offset:number, limit:number, sort:string): Observable<ITrigger[]> {
    console.log("DSFd")
    let params = ServiceUtils.buildQueryData(searchFields,offset,limit,sort);
    console.log("sdf")
    return this.httpclient.get<ITrigger[]>(this.baseUrl, {params}).pipe();
  }

  public get(triggerName, triggerGroup): Observable<ITrigger> {
    this.resp = this.httpclient.get<ITrigger>(this.baseUrl + '/' + triggerName + '/' + triggerGroup);
    return this.resp
  }

  public create(item: ITrigger): Observable<ITrigger> {
    return this.httpclient
      .post<ITrigger>(this.baseUrl, item).pipe();

  }
  public update(item: any, triggerName, triggerGroup): Observable<ITrigger> {
    return this.httpclient
      .put<ITrigger>(this.baseUrl + '/' + triggerName + '/' + triggerGroup, item).pipe();
  }
  public delete(triggerName, triggerGroup): Observable<null> {
    return this.httpclient
      .delete(this.baseUrl + '/' + triggerName + '/' + triggerGroup).pipe(map(res => null));
  }

  public getTriggerGroups(): Observable<string[]> {
    return this.httpclient.get<string[]>(this.baseUrl + "/getTriggerGroups").pipe();
  }

  public pauseTrigger(triggerName, triggerGroup): Observable<boolean> {
    return this.httpclient.get<boolean>(this.baseUrl + "/pauseTrigger/" + triggerName + '/' + triggerGroup).pipe();
  }

  public resumeTrigger(triggerName, triggerGroup): Observable<boolean> {
    return this.httpclient.get<boolean>(this.baseUrl + "/resumeTrigger/" + triggerName + '/' + triggerGroup).pipe();
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