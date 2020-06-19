
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ISpecialties } from './ispecialties';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SpecialtiesService extends GenericApiService<ISpecialties> { 
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl }, "specialties");
  }
  
  
  
}
