
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { GenericApiService, ISearchField, ServiceUtils } from 'projects/fast-code-core/src/public_api';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ShareApiService<T> extends GenericApiService<T> {
  constructor(protected http: HttpClient, resourceUrl: string) {
    super(http, { apiUrl: environment.apiUrl }, resourceUrl);
  }

  getShared(searchText?: string, offset?: number, limit?: number, sort?: string): Observable<T[]> {
    let params: any = {
      'search': searchText ? searchText : "",
      'offset': offset ? offset : 0,
      'limit': limit ? limit : 10,
      'sort': sort ? sort : ''
    };
    return this.http.get<T[]>(`${this.url}/shared`, { params }).pipe(catchError(this.handleError));
  }

  share(id: number, data): Observable<any> {
    return this.http.put(`${this.url}/${id}/share`, data).pipe(catchError(this.handleError));
  }

  unshare(id: number, data): Observable<any> {
    return this.http.put(`${this.url}/${id}/editAccess`, data).pipe(catchError(this.handleError));
  }

  updateRecipientSharingStatus(id: number, status: boolean): Observable<T> {
    return this.http.put<T>(`${this.url}/${id}/updateRecipientSharingStatus`, status.toString(), {headers:new HttpHeaders().set('Content-Type', 'application/json')}).pipe(catchError(this.handleError));
  }

  publish(id: number): Observable<any> {
    return this.http.put(`${this.url}/${id}/publish`, {}).pipe(catchError(this.handleError));
  }

  changeOwner(id: number, userId: number): Observable<T> {
    return this.http.put<T>(`${this.url}/${id}/changeOwner`, userId).pipe(catchError(this.handleError));
  }

  refresh(id: number): Observable<any> {
    return this.http.put(`${this.url}/${id}/refresh`, {}).pipe(catchError(this.handleError));
  }

  reset(id: number): Observable<T> {
    return this.http.put<T>(`${this.url}/${id}/reset`, {}).pipe(catchError(this.handleError));
  }

}
