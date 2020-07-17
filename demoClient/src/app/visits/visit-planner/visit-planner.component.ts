import {
  Component,
  ChangeDetectionStrategy,
  ViewChild,
  TemplateRef, Input, OnInit, ChangeDetectorRef
} from '@angular/core';
import {
  startOfDay,
  endOfDay,
  subDays,
  addDays,
  endOfMonth,
  isSameDay,
  isSameMonth,
  addHours,
  addMinutes,
} from 'date-fns';
import { Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent,
  CalendarView,
} from 'angular-calendar';

import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { VisitsService } from '../visits.service';
import { Router, ActivatedRoute } from '@angular/router';
// import { VisitsNewComponent } from '../visits-new.component';

// import { PetsService } from '../../pets/pets.service';
// import { VetsService } from '../../vets/vets.service';
import { GlobalPermissionService } from '../../core/global-permission.service';
// import { AuthenticationService } from '../../core/authentication.service';

import { IVisits, IChangeStatusObj, VisitStatus } from '../ivisits';
import { BaseListComponent, Globals, IListColumn, listColumnType, PickerDialogService, ErrorService, ConfirmDialogComponent, ITokenRole } from 'projects/fast-code-core/src/public_api';
// import { BaseListComponent, Globals, ErrorService} from 'projects/fast-code-core/src/public_api';

const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3',
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF',
  },
  yellow: {
    primary: '#e3bc08',
    secondary: '#FDF1BA',
  },
};

@Component({
  selector: 'visit-planner-component',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['visit-planner.component.scss'],
  templateUrl: 'visit-planner.component.html',
})
export class VisitPlannerComponent extends BaseListComponent<IVisits> implements OnInit {
  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;
  events: CalendarEvent[] = [];
  @Input() items;
  item = [
    // {description:'demo visit1',id:2,visitDate:'2020-07-17T06:00:00.000+0000',petId:1,petsDescriptiveField:'Mrs. Norris',vetId:2,vetsDescriptiveField:'strange',status:'COMPLETED',visitNotes:'visit completed',version:2},
    {description:'demo visit2',id:2,visitDate:'Wed Jul 17 2020 03:00:00 GMT+0500 (Pakistan Standard Time)',petId:1,petsDescriptiveField:'Mrs. Norris',vetId:2,vetsDescriptiveField:'strange',status:'COMPLETED',visitNotes:'visit completed',version:2},
    // {description:'demo visit3',id:2,visitDate:'Wed Jul 15 2020 05:00:00 GMT+0500 (Pakistan Standard Time)',petId:1,petsDescriptiveField:'Mrs. Norris',vetId:2,vetsDescriptiveField:'strange',status:'COMPLETED',visitNotes:'visit completed',version:2},
    // {description:'demo visit3',id:2,visitDate:'Wed Jul 15 2020 07:00:00 GMT+0500 (Pakistan Standard Time)',petId:1,petsDescriptiveField:'Mrs. Norris',vetId:2,vetsDescriptiveField:'strange',status:'COMPLETED',visitNotes:'visit completed',version:2}
  ];

  view: CalendarView = CalendarView.Month;

  CalendarView = CalendarView;

  viewDate: Date = new Date();

  modalData: {
    action: string;
    event: CalendarEvent;
  };

  actions: CalendarEventAction[] = [
    {
      label: '<i class="fas fa-fw fa-pencil-alt"></i>',
      a11yLabel: 'Edit',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
      },
    },
    {
      label: '<i class="fas fa-fw fa-trash-alt"></i>',
      a11yLabel: 'Delete',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter((iEvent) => iEvent !== event);
        this.handleEvent('Deleted', event);
      },
    },
  ];

  refresh: Subject<any> = new Subject();


  // events: CalendarEvent[] = [
  //   {
  //     start: subDays(startOfDay(new Date()), 1),
  //     end: addDays(new Date(), 1),
  //     title: 'A 3 day event',
  //     color: colors.red,
  //     actions: this.actions,
  //     allDay: true,
  //     resizable: {
  //       beforeStart: true,
  //       afterEnd: true,
  //     },
  //     draggable: true,
  //   },
  //   {
  //     start: startOfDay(new Date()),
  //     title: 'An event with no end date',
  //     color: colors.yellow,
  //     actions: this.actions,
  //   },
  //   {
  //     start: subDays(endOfMonth(new Date()), 3),
  //     end: addDays(endOfMonth(new Date()), 3),
  //     title: 'A long event that spans 2 months',
  //     color: colors.blue,
  //     allDay: true,
  //   },
  //   {
  //     start: addHours(startOfDay(new Date()), 2),
  //     end: addHours(new Date(), 2),
  //     title: 'A draggable and resizable event',
  //     color: colors.yellow,
  //     actions: this.actions,
  //     resizable: {
  //       beforeStart: true,
  //       afterEnd: true,
  //     },
  //     draggable: true,
  //   },
  // ];

  activeDayIsOpen: boolean = true;

  constructor(public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public dataService: VisitsService,
		public errorService: ErrorService,
		// public petsService: PetsService,
		// public vetsService: VetsService,
		public globalPermissionService: GlobalPermissionService)
    // public authenticationService: AuthenticationService)
    {
    // super(router, route, global, dataService, errorService)
    super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, dataService, errorService)
	}


  ngOnInit() {

    // this.item.forEach(element  => {
      for (let i = 0 ; i < this.item.length; i++ ) {
        let element = this.item[i];
      console.log(addHours(new Date(element.visitDate), 0));
      var data = {
        title: element.description + ' (Vet: ' + element.vetsDescriptiveField + ', Owner: ' + element.petsDescriptiveField + ')',
        start: new Date(element.visitDate),
        end: addMinutes(new Date(element.visitDate), 30),
        color: colors.red,
        draggable: false,
        actions: this.actions,
        index: i,
        resizable: {
          beforeStart: false,
          afterEnd: false,
        },
      };
      this.events.push(data);
    }
    // });
  }

  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
      this.viewDate = date;
    }
  }

  eventTimesChanged({
    event,
    newStart,
    newEnd,
  }: CalendarEventTimesChangedEvent): void {
    this.events = this.events.map((iEvent) => {
      if (iEvent === event) {
        return {
          ...event,
          start: newStart,
          end: newEnd,
        };
      }
      return iEvent;
    });
    this.handleEvent('Dropped or resized', event);
  }

  handleEvent(action: string, event: CalendarEvent): void {
    // this.modalData = { event, action };
    // this.modal.open(this.modalContent, { size: 'lg' });
    console.log(action);
    if (action == 'Edited') {
      var data= event;
      console.log(event);
      console.log(this.item[data.index]);
      let i = this.item[data.index];
      this.openDetails(this.item[i]);
    } else if(action == 'Deleted') {
      var data= event;
      // console.log(this.item[data.index]);
      // this.delete(this.item[data.index]);
      this.deleteEvent(event);
      console.log(event);
    }
  }

  addEvent(): void {
    this.events = [
      ...this.events,
      {
        title: 'New event',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
        color: colors.red,
        draggable: true,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
      },
    ];

    console.log(this.events);
  }

  deleteEvent(eventToDelete: CalendarEvent) {
    this.events = this.events.filter((event) => event !== eventToDelete);
  }

  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

}
