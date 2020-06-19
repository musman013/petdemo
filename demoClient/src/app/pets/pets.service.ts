
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IPets } from './ipets';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PetsService extends GenericApiService<IPets> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "pets");
  }
  
  
  
}
