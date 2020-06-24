
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { GenericApiService } from 'projects/fast-code-core/src/public_api';
import { environment } from '../../../../environments/environment';
import { IPermalink } from '../permalink/ipermalink';


@Injectable({
  providedIn: 'root'
})
export class ResourceViewService {
// export class ResourceViewService extends GenericApiService<IPermalink> {

  constructor(private httpclient: HttpClient) {
  //   super(httpclient, { apiUrl: environment.apiUrl }, "viewResource");
  }

  getResource(id): Observable<any> {
    return this.httpclient.get(environment.apiUrl + `/viewResource/${id}`);
  }
}
