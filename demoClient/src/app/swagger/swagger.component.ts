import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import SwaggerUI from 'swagger-ui';
import { environment } from 'src/environments/environment';
import { AuthenticationService } from 'src/app/core/authentication.service';

@Component({
  selector: 'app-swagger',
  templateUrl: './swagger.component.html',
  styleUrls: ['./swagger.component.css']
})
export class SwaggerComponent implements OnInit, AfterViewInit {

  @ViewChild('swagger', { static: false }) el;
  constructor(private authService: AuthenticationService) {

  }

  ngOnInit() {
  }
	
	ngAfterViewInit(): void {
    SwaggerUI({
      domNode: this.el.nativeElement,
      url: environment.apiUrl + "/v3/api-docs",
      deepLinking: true,
      requestInterceptor: this.requestInterceptor,
      presets: [
        SwaggerUI.presets.apis,
      ],
    })
  }
  
  requestInterceptor = (request) => {
    let token = this.authService.token;

    if (token) {
      request.headers["Authorization"] = token;
    }

    return request;
  }
}
