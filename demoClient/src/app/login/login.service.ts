import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ILogin } from './ilogin';
import {GenericApiService} from 'projects/fast-code-core/src/public_api';
@Injectable({
  providedIn: 'root'
})
export class LoginService extends GenericApiService<ILogin> {
  //environment.apiUrl;//'https://jsonplaceholder.typicode.com/users';
 
  
  constructor(private httpclient: HttpClient) { 
    super(httpclient, { apiUrl: environment.apiUrl },'roles');
    //super.sufix
  }
  /*getUsers(): Observable<IRole[]> {
   // this.create(this.user);
    return this.getAll();
  }
  getUser(id: number): Observable<IRole | undefined> {
   
   
    return this.getById(id);
  }*/
  
}