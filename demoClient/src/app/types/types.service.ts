
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ITypes } from './itypes';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TypesService extends GenericApiService<ITypes> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "types");
  }
  
  
  
}
