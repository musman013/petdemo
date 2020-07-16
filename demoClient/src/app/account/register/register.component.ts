import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { first } from 'rxjs/operators';
import { RegisterService } from '../register/register.service';
import { ErrorService, Globals } from 'projects/fast-code-core/src/public_api';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  itemForm: FormGroup;
  loading = false;
  submitted = false;

  constructor(
    public formBuilder: FormBuilder,
    public router: Router,
    public global: Globals,
    public errorService: ErrorService,
    public registerService: RegisterService
  ) { }

  ngOnInit() {
    this.setForm();
  }

  setForm() {
    this.itemForm = this.formBuilder.group({
      emailAddress: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      phoneNumber: [''],
      userName: ['', Validators.required],
      address: [''],
      city: [''],
    });
  }

  onSubmit() {
    // stop here if form is invalid
    if (this.itemForm.invalid) {
      return;
    }

    this.submitted = true;
    this.loading = true;
    let user = this.itemForm.getRawValue();
    this.registerService.register(user)
      .pipe(first())
      .subscribe(
        data => {
          this.loading = false;
          this.router.navigate(['register-complete'], { queryParams: { email: user.emailAddress } })
        },
        error => {
          this.loading = false;
          this.errorService.showError(error);
        });
  }

}
