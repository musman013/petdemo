import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { IForRootConf } from '../../IForRootConf';
import { IP_CONFIG } from '../../tokens';

@Injectable({
    providedIn: 'root'
})

export class RelatedContentService {

    url = "";
    resp: any
    constructor(private httpclient: HttpClient, @Inject(IP_CONFIG) private _config: IForRootConf) {
        this.url = _config.apiPath;
    }

    deleteContent(contentId): Observable<any> {
        return this.httpclient.delete(this.url + '/app/rest/content/' + contentId);
    };

    addRelatedContent(taskId, processInstanceId, caseInstanceId, file, isIE): Observable<any> {
        var uploadPromise;
        var url;
        // url = this.url + '/app/rest/tasks/' + taskId + '/raw-content';

        let formData: FormData = new FormData();
        formData.append('file', file, file.name);
        let headers = new HttpHeaders();
        /** In Angular 5, including the header Content-Type can invalidate your request */
        headers.append('Content-Type', 'multipart/form-data');
        headers.append('Accept', 'application/json');
        let options = { headers: headers };

        // return this.httpclient.post(this.url + '/app/rest/tasks/' + taskId + '/raw-content', formData, options);

        if (taskId) {
            if (isIE) {
                url = this.url + '/app/rest/tasks/' + taskId + '/raw-content/text';
            } else {
                url = this.url + '/app/rest/tasks/' + taskId + '/raw-content';
            }
        }
        else if (processInstanceId) {
            if (isIE) {
                url = this.url + '/app/rest/process-instances/' + processInstanceId + '/raw-content/text';
            } else {
                url = this.url + '/app/rest/process-instances/' + processInstanceId + '/raw-content';
            }
            // uploadPromise = Upload.upload({
            //     url: url,
            //     method: 'POST',
            //     file: file
            // });
        }
        else {
            if (isIE) {
                url = this.url + '/app/rest/content/raw/text';
            } else {
                url = this.url + '/app/rest/content/raw';
            }
        }

        return this.httpclient.post(url, formData, options);

    };

    addRelatedContentFromSource(taskId, processInstanceId, source, sourceId, name, link) {
        var url;
        if (taskId) {
            url = this.url + '/app/rest/tasks/' + taskId + '/content';
        } else {
            url = this.url + '/app/rest/content';
        }

        // Force a boolean value to be sent in the response body
        link = (link == true);

        var data = {
            source: source,
            sourceId: sourceId,
            name: name,
            link: link
        };

        var service = this;

        // $http(
        //     {
        //         method: 'POST',
        //         url: url,
        //         data: data
        //     }
        // ).success(function (response, status, headers, config) {
        //     if (response && response.id) {
        //         service.addUrlToContent(response);
        //     }
        // })
        //     .error(function (response, status, headers, config) {
        //         deferred.reject(response);
        //     });


        // var promise = deferred.promise;
    };

    getRelatedContent(id): Observable<any> {

        var resp = this.httpclient.get(this.url + '/app/rest/content/' + id);
        resp.subscribe((response: any) => {
            if (response && response.id) {
                this.addUrlToContent(response);
            }
        });
        return resp;
    };

    addUrlToContent(content) {
        if (content && content.id) {
            content.rawUrl = this.url + '/app/rest/content/' + content.id + "/raw";

            var fileExtenstion: string = content.name.split('.').pop();
            if (fileExtenstion)
                fileExtenstion = fileExtenstion.toLowerCase()

            if (!content.link && (content.simpleType == 'word' || content.simpleType == 'excel' || content.simpleType == 'powerpoint')) {
                content.officeUrl = this.url + '/aos/' + content.id + "/" + content.name;
            }

            if (content.thumbnailStatus == 'created') {
                content.thumbnailUrl = this.url + '/app/rest/content/' + content.id + "/rendition/thumbnail?noCache=" + new Date().getTime();
            }

            if (content.previewStatus == 'created') {
                content.pdfUrl = this.url + '/app/rest/content/' + content.id + "/rendition/preview?noCache=" + new Date().getTime();
            } else if (content.simpleType === 'image' || fileExtenstion === 'jpg' || fileExtenstion === 'jpeg' || fileExtenstion === 'png') {
                content.imageUrl = content.rawUrl;
                content.thumbnailUrl = content.rawUrl;
            } else if (content.simpleType == 'pdf') {
                content.pdfUrl = content.rawUrl;
            }

        }
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