
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IPermalink } from './ipermalink';
import { GenericApiService } from 'projects/fast-code-core/src/public_api';
import { environment } from '../../../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class PermalinkService extends GenericApiService<IPermalink> {

  constructor(private httpclient: HttpClient) {
    super(httpclient, { apiUrl: environment.apiUrl }, "permalink");
  }

}
