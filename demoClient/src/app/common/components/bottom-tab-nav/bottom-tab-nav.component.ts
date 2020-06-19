import {Component,Output, EventEmitter} from '@angular/core';

/**
 * @title Tab group with the headers on the bottom
 */
@Component({
  selector: 'bottom-tab-nav',
  templateUrl: './bottom-tab-nav.component.html',
  styleUrls: ['./bottom-tab-nav.component.scss'],
})
export class BottomTabNavComponent {
  @Output() onNavMenuClicked: EventEmitter<any> = new EventEmitter<any>();
  onNavMenuClick() {
    this.onNavMenuClicked.emit(true);
  }
}
