
import { Injectable,Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {  IEmailVariable } from './iemail-variable';
//import {GenericApiService} from '../core/generic-api.service';

import { IP_CONFIG } from '../../tokens';
//import { ILibraryRootConfg } from '../../interfaces';
//import { GenericApiService,ILibraryRootConfg } from 'fastCodeCore'; //from 'projects/fast-code-core/src/lib/common/core/generic-api.service';
import { GenericApiService ,ILibraryRootConfg} from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
import { IEmailVariableType } from './iemail-variable-type';
import { Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class EmailVariablTypeService extends GenericApiService<IEmailVariableType> { 
  constructor(private httpclient: HttpClient,@Inject(IP_CONFIG)  config: ILibraryRootConfg) { 
    super(httpclient,{apiUrl:config.apiPath} ,"variable-types");
  }

  getAllMasterValue(masterName): Observable<string[]> {
    let url = this.apiUrl + '/master/getMastersByMasterName?name=' + masterName;
    return this.httpclient.get<string[]>(url).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }
  
  
}
