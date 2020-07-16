import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-register-complete',
  templateUrl: './register-complete.component.html',
  styleUrls: ['./register-complete.component.scss']
})
export class RegisterCompleteComponent implements OnInit {

  email = "";
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute
    ) { }

  ngOnInit() {
    this.email = this.activatedRoute.snapshot.queryParamMap.get("email");
  }

  GotoLogin() {
    this.router.navigate(['login']);
  }

}
