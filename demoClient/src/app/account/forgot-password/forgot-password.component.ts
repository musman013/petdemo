import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ReplaySubject } from 'rxjs';
import { AuthenticationService } from 'src/app/core/authentication.service';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  resetForm: FormGroup;
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);
  disabled: any = false;
  linkSent: boolean = false;


  returnUrl: string = 'fastcode';

  constructor(
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit() {
    this.form();
  }

  form() {
    this.resetForm = new FormGroup({
      email: new FormControl('')
    });
  }

  forgotPassword() {

    this.disabled = true;
    if (this.resetForm.invalid) {
      this.disabled = false;
      return;
    }

    this.authenticationService.forgotPassword(this.resetForm.value.email)
    .pipe(takeUntil(this.destroyed$))
      .subscribe(
        data => {
          this.linkSent = true;
        },
        error => {
          this.disabled = false;
        });
  }

  ngOnDestroy() {
    this.destroyed$.next(true);
    this.destroyed$.unsubscribe();
  }

}
