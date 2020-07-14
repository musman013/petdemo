import { Component, OnInit, Inject } from '@angular/core';
import { IUser } from 'src/app/admin/user-management/user/index';
import { Router } from "@angular/router";
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { first } from 'rxjs/operators';
import { GlobalPermissionService } from '../core/global-permission.service';
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
    console.log(this.itemForm);
    this.registerService.register(this.itemForm.getRawValue())
      .pipe(first())
      .subscribe(
        data => {
          this.loading = false;
          console.log(data);
        },
        error => {
          this.loading = false;
        });
  }

}
