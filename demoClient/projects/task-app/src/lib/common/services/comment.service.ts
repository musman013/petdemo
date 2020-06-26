import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { IForRootConf } from '../../IForRootConf';
import { IP_CONFIG } from '../../tokens';
@Injectable({
    providedIn: 'root'
})
// export class TriggerService extends GenericApiService<ITrigger> {
export class CommentService {

    url = "";
    resp: any
    constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) private _config: IForRootConf) {
        this.url = _config.apiPath;
    }

    /*
    * Get all comments on a task
    */
    public getTaskComments(taskId): Observable<any> {
        return this.httpclient.get(this.url + '/app/rest/tasks/' + taskId + '/comments?latestFirst=true');
    };

    /*
    * Create a new comment on a task
    */
    public createTaskComment(taskId, message): Observable<any> {

        var data = { message: message };
        return this.httpclient.post(this.url + '/app/rest/tasks/' + taskId + '/comments', data);
    };

    /*
    * Get all comments on a process instance
    */
    public getProcessInstanceComments(processInstanceId): Observable<any> {
        return this.httpclient.get(this.url + '/app/rest/process-instances/' + processInstanceId + '/comments?latestFirst=true');
    };

    /*
    * Create a new comment on a process instance
    */
    public createProcessInstanceComment(processInstanceId, message): Observable<any> {

        var data = { message: message };
        return this.httpclient.post(this.url + '/app/rest/process-instances/' + processInstanceId + '/comments', data)
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