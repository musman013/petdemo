//
// ===== File globals.ts    
//
'use strict';
import { Injectable } from "@angular/core";
import { BreakpointObserver } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
@Injectable()
export class Globals {

    languages:Array<String> =[];
    default_language: string="";
    constructor(private breakpointObserver: BreakpointObserver) {
    
     
    }
    smaillDeviceBreakpoint:'768px';
    mediumDeviceBreakpoint:'1024px';
    isSmallDevice$: Observable<boolean> = this.breakpointObserver.observe(['(max-width: 768px)'])
    .pipe(
      map(result => result.matches)
    );
    isMediumDevice$: Observable<boolean> = this.breakpointObserver.observe(['(min-width: 768px)  and (max-width: 1024px)'])
    .pipe(
      map(result => result.matches)
    );
    isMediumDeviceOrLess$: Observable<boolean> = this.breakpointObserver.observe(['(min-width: 0px)  and  (max-width: 1024px)'])
    .pipe(
      map(result => result.matches)
    );
    isLargeDevice$: Observable<boolean> = this.breakpointObserver.observe(['(min-width: 1024px) '])
    .pipe(
      map(result => result.matches)
    );
    getCookie(cname) {
        var name = cname + "=";
        var decodedCookie = decodeURIComponent(document.cookie);
        var ca = decodedCookie.split(';');
        for(var i = 0; i <ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    } 

}
