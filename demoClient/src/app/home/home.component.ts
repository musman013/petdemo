import { Component } from '@angular/core';
import { Router} from "@angular/router";
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../core/authentication.service';
 
@Component({ 
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	constructor(
		public router: Router,
		public authenticationService: AuthenticationService,
  ) { }
 
  onSubmit() {
    this.router.navigate(['/login'], { queryParams: { returnUrl: 'dashboard' } });
  }
	goToDashboard(){
    this.router.navigate(['dashboard']);
  }
}
