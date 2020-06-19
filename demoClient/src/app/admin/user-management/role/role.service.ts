
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IRole } from './irole';
import {GenericApiService} from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RoleService extends GenericApiService<IRole> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "role");
  }
  
  
}
