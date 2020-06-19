
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IUserrole } from './iuserrole';
import { GenericApiService } from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserroleService extends GenericApiService<IUserrole> { 
  
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "userrole");
  }
  
}
