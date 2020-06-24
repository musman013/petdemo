import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit {
  @Output() onRedirect = new EventEmitter<void>();
  constructor() { }

  ngOnInit() {
  }

  onChoosingPage(){
    this.onRedirect.emit();
  }

}
