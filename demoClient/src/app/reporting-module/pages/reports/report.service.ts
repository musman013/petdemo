
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { IReport } from './Ireport';
import { ShareApiService } from '../../services/share-api.service';

@Injectable({
  providedIn: 'root'
})
export class ReportService extends ShareApiService<IReport>{
  constructor(private httpClient: HttpClient) {
      super(httpClient, "report");
  }

  public getAllReports(searchText?: string, offset?: number, limit?: number, sort?: string): Observable<IReport[]> {

    let params: any = {
      'search': searchText ? searchText : "",
      'offset': offset ? offset : 0,
      'limit': limit ? limit : 10,
      'sort': sort ? sort : ''
    };
    return this.http.get<IReport[]>(this.url, { params }).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));

  }
  
}
