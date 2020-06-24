import { Injectable } from '@angular/core';
import { MainService } from './main.service';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private service: MainService,
    private router: Router
) { }

  canActivate(route: ActivatedRouteSnapshot, router: RouterStateSnapshot):
    | boolean
    | UrlTree
    | Promise<boolean | UrlTree>
    | Observable<boolean | UrlTree> {
    let user  =  this.service.getToken();
    const isAuth = !!user;
    if (isAuth) {
        return true;
    }
    return this.router.createUrlTree(['/login']);
  }
}
