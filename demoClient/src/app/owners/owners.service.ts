
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IOwners } from './iowners';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OwnersService extends GenericApiService<IOwners> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "owners");
  }
  
  
  
}
