import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';

import { UserService } from '../../services/user.service';
@Component({
  selector: 'app-user-picture',
  templateUrl: './user-picture.component.html',
  styleUrls: ['./user-picture.component.scss']
})
export class UserPictureComponent implements OnInit, OnChanges {
  @Input() userId: any;

  url = "";
  userPic: any = { text: '' };
  username: string = "";
  user: any = {};

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.getUser();
    this.url = this.userService.url;
  }


  getUser() {
    if (this.userId) {
      this.userService.getUserInfo(this.userId).subscribe((response) => {
        this.user = response;
        this.setUserPicture();
      })
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    for (let propName in changes) {
      if (propName === 'userId') {
        this.userId = changes[propName].currentValue;
        this.getUser();
      }
    }
  }

  setUserPicture() {
    // this.username = this.user.firstName && this.user.firstName != "null" ? this.username + this.user.firstName : this.username + "";
    // this.username = this.user.lastName && this.user.lastName != "null" ? this.username + " " + this.user.lastName : this.username + "";

    if (this.user.pictureId) {
      this.userPic.class = "user-picture";
      this.userPic.style = 'url("' + this.url + '/app/rest/users/' + this.user.id + '/picture")';
    } else {
      this.userPic.class = "user-picture no-picture";
    }

    if (this.user.firstName && this.user.lastName) {
      this.userPic.text = this.user.firstName.substring(0, 1).toUpperCase() + this.user.lastName.substring(0, 1).toUpperCase();
      this.userPic.userName = this.user.firstName + ' ' + this.user.lastName;
    } else if (this.user.lastName != undefined && this.user.lastName != null) {
      if (this.user.lastName.length > 1) {
        this.userPic.text = this.user.lastName.substring(0, 2).toUpperCase();
      } else if (this.user.lastName.length == 1) {
        this.userPic.text = this.user.lastName.substring(0, 1).toUpperCase();
      }
      this.userPic.userName = this.user.lastName;
    } else if (this.user.firstName != undefined && this.user.firstName != null) {
      if (this.user.firstName.length > 1) {
        this.userPic.text = this.user.firstName.substring(0, 2).toUpperCase();
      } else if (this.user.firstName.length == 1) {
        this.userPic.text = this.user.firstName.substring(0, 1).toUpperCase();
      }
      this.userPic.userName = this.user.firstName;
    } else {
      if (this.user != undefined && this.user != null) {
        if (this.user.length > 1) {
          this.userPic.text = this.user.substring(0, 2).toUpperCase();
        } else if (this.user.length == 1) {
          this.userPic.text = this.user.substring(0, 1).toUpperCase();
        }
        this.userPic.userName = this.user;

      } else {
        this.userPic.text = '??';
        this.userPic.userName = '';
      }
    }
  }

}
