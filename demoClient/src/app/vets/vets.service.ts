
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IVets } from './ivets';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class VetsService extends GenericApiService<IVets> {
  constructor(private httpclient: HttpClient) {
    super(httpclient, { apiUrl: environment.apiUrl }, "vets");
  }

  /**
* Call api to update user profile.
* @param item
* @returns Observable of updated entity object.
*/
  public updateProfile(user: IVets): Observable<IVets> {
    return this.httpclient
      .put<IVets>(this.url + '/updateProfile', user).pipe(catchError(this.handleError));
  }

  getProfile() {
    return this.httpclient.get<IVets>(this.url + '/getProfile').pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError))
  }


}
