import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MainService } from '../../services/main.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private service: MainService) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      userName: ['admin', Validators.required],
      password: ['secret', Validators.required]
    });
  }

  login(){
    // console.log(this.loginForm);
    if(this.loginForm.valid){
      this.service.login(this.loginForm.value)
    }
  }

}
