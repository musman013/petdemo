import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { IForRootConf } from '../IForRootConf';
import { IP_CONFIG } from '../tokens';

@Injectable({
    providedIn: 'root'
})
// export class TriggerService extends GenericApiService<ITrigger> {
export class TaskService {

    selectedTask:any;
    url = "";
    resp: any
    constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) private _config: IForRootConf) {
        this.url = _config.apiPath;
    }

    public get(taskId): Observable<any> {
        return this.httpclient.get(this.url + '/app/rest/tasks/' + taskId);
    }

    public getProcessInstanceTasks(processInstanceId, isCompleted): Observable<any> {

        let data: any = {};
        data.processInstanceId = processInstanceId

        if (isCompleted) {
            data.state = 'completed';
        }
        return this.httpclient.post(this.url + '/app/rest/query/tasks', data);
    };

    public involveUserInTask(userId, taskId) {
        console.log(userId);
        let involveData = {
            userId: userId
        };

        return this.httpclient.put(this.url + '/app/rest/tasks/' + taskId + '/action/involve', involveData);
    };

    public involveUserInTaskByEmail(email, taskId) {

        let involveData = {
            email: email
        };

        return this.httpclient.put(this.url + '/app/rest/tasks/' + taskId + '/action/involve', involveData);
    };

    public removeInvolvedUserInTask(user, taskId) {

        let removeInvolvedData: any = {};
        if (user.id !== null && user.id !== undefined) {
            removeInvolvedData.userId = user.id;
        } else {
            removeInvolvedData.email = user.email;
        }

        return this.httpclient.put(this.url + '/app/rest/tasks/' + taskId + '/action/remove-involved', removeInvolvedData);
    };

    public queryTasks(data): Observable<any> {
        return this.httpclient.post(this.url + '/app/rest/query/tasks', data);
    };

    /**
     * Simple completion of a task without submitting form-data.
     */
    public completeTask(taskId) {
        return this.httpclient.put(this.url + '/app/rest/tasks/' + taskId + '/action/complete', {});
    }

    public claimTask(taskId) {
        return this.httpclient.put(this.url + '/app/rest/tasks/' + taskId + '/action/claim', {});
    };

    public getRelatedContent(taskId) {
        return this.httpclient.get(this.url + '/app/rest/tasks/' + taskId + '/content');
    };

    public getSubTasks(taskId) {
        return this.httpclient.get(this.url + '/app/rest/tasks/' + taskId + '/subtasks');
    };

    public assignTask(taskId, userId) {

        var assignData = {
            assignee: userId
        };

        return this.httpclient.put(this.url + '/app/rest/tasks/' + taskId + '/action/assign', assignData);
    };

    public assignTaskByEmail(taskId, email) {

        var assignData = {
            email: email
        };

        return this.httpclient.put(this.url + '/app/rest/tasks/' + taskId + '/action/assign', assignData);
    };

    public updateTask(taskId, data): Observable<any> {
        return this.httpclient.put(this.url + '/app/rest/tasks/' + taskId, data);
    };

    public createTask(taskData) {
        return this.httpclient.post(this.url + '/app/rest/tasks/', taskData);
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