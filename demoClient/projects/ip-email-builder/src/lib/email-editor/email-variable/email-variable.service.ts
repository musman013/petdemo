
import { Injectable,Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {  IEmailVariable } from './iemail-variable';
//import {GenericApiService} from '../core/generic-api.service';

import { IP_CONFIG } from '../../tokens';
//import { ILibraryRootConfg } from '../../interfaces';
//import { GenericApiService,ILibraryRootConfg } from 'fastCodeCore'; //from 'projects/fast-code-core/src/lib/common/core/generic-api.service';
import { GenericApiService ,ILibraryRootConfg} from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
@Injectable({
  providedIn: 'root'
})
export class EmailVariableService extends GenericApiService<IEmailVariable> { 
  constructor(private httpclient: HttpClient,@Inject(IP_CONFIG)  config: ILibraryRootConfg) { 
    super(httpclient,{apiUrl:config.apiPath} ,"emailvariable");
    
   
  }
  
  
}
