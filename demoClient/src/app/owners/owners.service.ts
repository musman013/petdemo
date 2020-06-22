
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IOwners } from './iowners';
import { GenericApiService } from '../../../projects/fast-code-core/src/public_api';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class OwnersService extends GenericApiService<IOwners> {
  constructor(private httpclient: HttpClient) {
    super(httpclient, { apiUrl: environment.apiUrl }, "owners");
  }

  /**
* Call api to update user profile.
* @param item
* @returns Observable of updated entity object.
*/
  public updateProfile(user: IOwners): Observable<IOwners> {
    return this.httpclient
      .put<IOwners>(this.url + '/updateProfile', user).pipe(catchError(this.handleError));
  }

  getProfile() {
    return this.httpclient.get<IOwners>(this.url + '/getProfile').pipe(map((response: any) => {
      return response;
    }), catchError(this.handleError))
  }


}
