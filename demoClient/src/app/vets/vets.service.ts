
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IVets } from './ivets';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VetsService extends GenericApiService<IVets> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "vets");
  }
  
  
  
}
