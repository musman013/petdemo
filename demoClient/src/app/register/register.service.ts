import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { IOwners } from '../owners';
import { throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  url = environment.apiUrl + "/register";
  constructor(private httpclient: HttpClient) {}

  register(owner: IOwners){
    return this.httpclient.post(this.url, owner).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }

  verifyEmail(token: string){
    return this.httpclient.get(`${this.url}/verifyEmail?token=${token}`).pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError));
  }

  /**
   * Handles Api error events.
   * @param err 
   */
  protected handleError(err: HttpErrorResponse) {

    let errorMessage = '';
    if (err.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errorMessage = 'An error occurred: ' + err.error.message;
    } else {
      console.log(err);
      errorMessage = 'Server returned code: ' + err.status + ', error message is: ' + err.message;
    }
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}
