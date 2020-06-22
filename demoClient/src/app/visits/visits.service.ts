
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IVisits } from './ivisits';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VisitsService extends GenericApiService<IVisits> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "visits");
  }

  changeStatus(status): Observable<IVisits>{
    return this.httpclient.put<IVisits>(`${this.apiUrl}/changeStatus`, {status: status});
  }
  
  
  
}
