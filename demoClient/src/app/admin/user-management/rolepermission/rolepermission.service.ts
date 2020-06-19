
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IRolepermission } from './irolepermission';
import {GenericApiService} from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RolepermissionService extends GenericApiService<IRolepermission> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "rolepermission");
  }
  
  
}
