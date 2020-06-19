import { Injectable, Inject } from '@angular/core';
import { HttpClient,HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { environment } from '../environment';
import { IP_CONFIG } from '../tokens';
//import { ILibraryRootConfg } from '../interfaces';
//import {ILibraryRootConfg } from 'fastCodeCore';
import { ILibraryRootConfg} from 'projects/fast-code-core/src/public_api';// 'fastCodeCore';
//import { IUser } from '../users/iuser';

const API_URL = environment.apiUrl;

@Injectable()
export class GenericApiService<T> {
    private url = environment.apiUrl;
    private suffix = '';
  constructor(  private http: HttpClient, private  config: ILibraryRootConfg,suffix:string  ) {
    //this.url = environment.apiUrl + '/' + suffix;
    this.url = config.apiPath + '/' + suffix;
    this.suffix = suffix;
  }
  public buildQueryData(search?:string, offset?:any, limit?:any,sort?:string):any {
    let params = {
      'search': search?search:'',
      'offset': offset?offset:0,
      'limit': limit?limit:10,
      'sort': sort? sort:''
    }
    /*
    const params:HttpParams = new HttpParams();
    
    if(search) params.set('search', search);
    if(offset) params.set('offset', offset); else params.set('offset', '0');
    
    if(limit) params.set('limit', limit);
    if(sort) params.set('sort', sort);*/

    return params;
 }

  public getAll(search?:string, offset?:number, limit?:number, sort?:string): Observable<T[]> {
   
    let params = this.buildQueryData(search,offset,limit,sort);
    
    return this.http.get<T[]>(this.url,{params}).pipe(map((response:any)=>{
      return response;
    }), catchError(this.handleError));
     
  }
  getAssociations(parentSuffix: string,parentId:any,search?:string, offset?:number, limit?:number,sort?:string): Observable<T[]> {
    //let url = API_URL + '/' + parentSuffix + '/' + parentId + '/'+ this.suffix;
    let url = this.config.apiPath + '/' + parentSuffix + '/' + parentId + '/'+ this.suffix;
    
    let params = this.buildQueryData(search,offset,limit,sort);
    return this.http.get<T[]>(url,{params}).pipe(map((response:any)=>{
      return response;
    }), catchError(this.handleError));
    }
  /*public getChildren(parentUrl: string,search?:string, offset?:number, limit?:number, sort?:string): Observable<T[]> {
   
    let params = this.buildQueryData(search,offset,limit,sort);
    
    return this.http.get<T[]>(API_URL + '/' + parentUrl + '/'+ this.suffix,{params}).pipe(map((response:any)=>{
      return response;
    }), catchError(this.handleError));
     
  }*/
  public getById(id: any): Observable<T> {
    return this.http
      .get<T>(this.url + '/' + id).pipe(catchError(this.handleError));
  }
  public create(item: T): Observable<T> {
    return this.http
      .post<T>(this.url, item).pipe(catchError(this.handleError));
     
  }
  public update(item: T,id:any): Observable<T> {
    return this.http
      .put<T>(this.url+ '/' + id, item).pipe(catchError(this.handleError));
  }
  public delete(id: any): Observable<null> {
    return this.http
      .delete(this.url + '/' + id).pipe(map(res=>null),catchError(this.handleError));    
  }
  public deleteAssociation(parentSuffix: string,parentId:any,id: any): Observable<null> {
    //let url = API_URL + '/' + parentSuffix + '/' + parentId + '/'+ this.suffix
    let url = this.config.apiPath + '/' + parentSuffix + '/' + parentId + '/'+ this.suffix;
     
    return this.http
      .delete(url + '/' + id).pipe(map(res=>null),catchError(this.handleError));    
  }

  protected handleError(err: HttpErrorResponse) {
  
    let errorMessage = '';
    if (err.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errorMessage = `An error occurred: ${err.error.message}`;
    } else {
      
      errorMessage = `Server returned code: ${err.status}, error message is: ${err.message}`;
    }
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}