import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RegisterService } from '../register/register.service';

@Component({
  selector: 'app-confirm-email',
  templateUrl: './confirm-email.component.html',
  styleUrls: ['./confirm-email.component.scss']
})
export class ConfirmEmailComponent implements OnInit {
  loading = false;
  confirmed = false;
  resetToken = "";
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private registerService: RegisterService
    ) { }

  ngOnInit() {
    this.resetToken = this.activatedRoute.snapshot.queryParamMap.get("token");
    if(this.resetToken){
      this.registerService.verifyEmail(this.resetToken).subscribe(resp => {
        if(resp){
          this.confirmed = true;
        }
      });
    }
  }

  GotoLogin() {
    this.router.navigate(['login']);
  }
}
