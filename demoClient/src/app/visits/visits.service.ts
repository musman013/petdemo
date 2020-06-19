
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IVisits } from './ivisits';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VisitsService extends GenericApiService<IVisits> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "visits");
  }
  
  
  
}
