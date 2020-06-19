import { JobNewComponent } from '../lib/jobs/index';
import { TriggerNewComponent, SelectJobComponent } from '../lib/triggers/index';
import { NgModule } from '@angular/core';

import { Injectable,  Input} from '@angular/core';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

//import { Observable } from 'rxjs';
//import 'rxjs/add/observable/of';
//import 'rxjs/add/observable/throw';
import { Observable, throwError,of } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import {Component, Directive, ChangeDetectorRef} from '@angular/core';

import {Router, ActivatedRoute,} from '@angular/router';
import {RouterTestingModule} from '@angular/router/testing';
import { PickerDialogService, PickerComponent, ListFiltersComponent } from 'projects/fast-code-core/src/public_api';;
import { Globals } from '../lib/globals';
import {MatDialog, MatDialogRef,MatButtonModule, MatToolbarModule, MatSidenavModule,
  MatIconModule, MatListModule, MatRadioModule, MatTableModule,
  MatCardModule, MatTabsModule, MatInputModule, MatDialogModule,
  MatSelectModule, MatCheckboxModule, MatAutocompleteModule,
  MatDatepickerModule, MatNativeDateModule, MatMenuModule, MatTable,
  MatChipsModule, MatSortModule, MatSnackBarModule
} from '@angular/material';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';

let translations: any = {"CARDS_TITLE": "This is a test"};

class FakeLoader implements TranslateLoader {
  getTranslation(lang: string): Observable<any> {
    return of(translations);
  }
}

  @Injectable()
  class MockRouter { navigate = (commands:any)=> {}; }
  @Injectable()
  class MockPickerDialogService { }
  @Directive({
  selector:'[routerlink]',
  host: {'(click)':'onClick()'}
  })
  export  class RouterLinkDirectiveStub {
    @Input('routerLink') linkParams: any;
    navigatedTo: any = null;
    onClick () {
      this.navigatedTo = this.linkParams;
    }
  }
  let mockGlobal = {
    isSmallDevice$: of({value:true}),
    isMediumDeviceOrLess$:of({value:true})
  };
  let mockActivatedRoute = {
    snapshot: {paramMap: {get: ()=> {return '1'}}},
    paramMap: of({
       convertToParamMap: { 
        userId: 'abc123',
      anotherId: 'd31e8b48-7309-4c83-9884-4142efdf7271',          
       }
      }),
    queryParams: of({
      convertToParamMap: { 
       userId: 'abc123',
     anotherId: 'd31e8b48-7309-4c83-9884-4142efdf7271',          
      }
     })
  }
   class BlankComponent {

  }
@NgModule({
  imports: [
    RouterTestingModule.withRoutes(
      [{path: 'users', component: BlankComponent}]
    ),HttpClientTestingModule,NoopAnimationsModule,
    FormsModule,ReactiveFormsModule,
     MatButtonModule, MatToolbarModule, MatSidenavModule,
    MatIconModule, MatListModule, MatRadioModule, MatTableModule,
    MatCardModule, MatTabsModule, MatInputModule, MatDialogModule,
    MatSelectModule, MatCheckboxModule, MatAutocompleteModule,
    MatDatepickerModule, MatNativeDateModule, MatMenuModule, MatChipsModule,
    MatSortModule, NgxMaterialTimepickerModule, MatSnackBarModule,
    TranslateModule.forRoot({
      loader: {provide: TranslateLoader, useClass: FakeLoader},
    })
  ],
  exports: [RouterTestingModule,HttpClientTestingModule,NoopAnimationsModule,
    FormsModule,ReactiveFormsModule,
     MatButtonModule, MatToolbarModule, MatSidenavModule,
    MatIconModule, MatListModule, MatRadioModule, MatTableModule,
    MatCardModule, MatTabsModule, MatInputModule, MatDialogModule,
    MatSelectModule, MatCheckboxModule, MatAutocompleteModule,
    MatDatepickerModule, MatNativeDateModule, MatMenuModule, MatTable,
    MatChipsModule, MatSortModule, NgxMaterialTimepickerModule, MatSnackBarModule,
    TranslateModule
  // , PickerComponent,UserNewComponent,JobNewComponent,TriggerNewComponent
  ],
  providers: [
   // {provide: Router, useClass: MockRouter},
   {provide: ActivatedRoute, useValue:mockActivatedRoute},
    {provide: Globals, useValue: mockGlobal},
    MatDialog,
    PickerDialogService,
   // {provide: PickerDialogService, useClass: MockPickerDialogService},
   
  ],
  entryComponents: [
    JobNewComponent,
    TriggerNewComponent,
    SelectJobComponent,
    PickerComponent,
    ListFiltersComponent
  ]  
 
})
export class TestingModule {  
  constructor() {}
}
export var EntryComponents:any[]=[
    JobNewComponent,
    TriggerNewComponent,
    SelectJobComponent,
    PickerComponent,
    ListFiltersComponent
];
