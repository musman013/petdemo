
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IPermission } from './ipermission';
import {GenericApiService} from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PermissionService extends GenericApiService<IPermission> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "permission");
  }
  
  
}
