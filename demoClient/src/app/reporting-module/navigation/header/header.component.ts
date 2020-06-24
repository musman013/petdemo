import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import { MainService } from '../../services/main.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  @Output() toggleSideNav = new EventEmitter<void>();
  constructor(private service: MainService) { }

  ngOnInit() {
  }

  logout(){
    this.service.logout();
  }

  onClickToggle(){
    this.toggleSideNav.emit();
  }
  
}
