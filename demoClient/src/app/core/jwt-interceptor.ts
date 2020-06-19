import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from './authentication.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor( private authService: AuthenticationService ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available

    let token = this.authService.token;

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: token,
          Accept: 'application/json'
        }
      });
    }

    return next.handle(request);
  }
}