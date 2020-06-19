
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IVetSpecialties } from './ivet-specialties';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VetSpecialtiesService extends GenericApiService<IVetSpecialties> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "vetSpecialties");
  }
  
  
  
}
