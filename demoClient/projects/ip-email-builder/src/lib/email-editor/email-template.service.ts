import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { IEmailTemplate } from './iemail-template';
//import {GenericApiService} from './generic-api.service';
import { IP_CONFIG } from '../tokens';
//import { ILibraryRootConfg } from '../interfaces';

//import {GenericApiService,ILibraryRootConfg} from 'fastCodeCore';
//import { GenericApiService ,ILibraryRootConfg} from 'fastCodeCore';
import { GenericApiService,ILibraryRootConfg } from 'projects/fast-code-core/src/public_api';//'projects/fast-code-core/src/lib/common/core/generic-api.service';
@Injectable({
  providedIn: 'root'
})
export class EmailTemplateService extends GenericApiService<IEmailTemplate> {
  //environment.apiUrl;//'https://jsonplaceholder.typicode.com/users';
 
  constructor(private httpclient: HttpClient,@Inject(IP_CONFIG)  config: ILibraryRootConfg) { 
   // super(httpclient,{xApiKey:config.xApiKey,apiPath:config.apiPath} ,'email');
   super(httpclient,{apiUrl:config.apiPath} ,'email');
   
  }
  
  
}