import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpParams,
  HttpEvent
} from '@angular/common/http';
import { take, exhaustMap, map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { MainService } from './main.service';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private service: MainService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add header with basic auth credentials if user is logged in and request is to the api url
    const user = this.service.getToken();
    const isLoggedIn = user && user.token;
    if (isLoggedIn) {
        request = request.clone({
            setHeaders: { 
                Authorization: `${user.token}`
            }
        });
    }

    return next.handle(request);
}
}
