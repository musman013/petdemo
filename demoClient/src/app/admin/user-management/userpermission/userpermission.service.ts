
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IUserpermission } from './iuserpermission';
import { GenericApiService } from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserpermissionService extends GenericApiService<IUserpermission> { 
  
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "userpermission");
  }
  
}
