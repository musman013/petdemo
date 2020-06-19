import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JobDetailsComponent, JobNewComponent, JobService, IJob } from '../index';

import { Injectable, CUSTOM_ELEMENTS_SCHEMA ,NO_ERRORS_SCHEMA, Input} from '@angular/core';
import { HttpTestingController } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { Observable, throwError,of } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { Component, Directive, ChangeDetectorRef} from '@angular/core';
import { TriggerNewComponent } from '../../triggers/index';

import { TestingModule } from '../../../testing/utils';
import { environment } from '../../environment';

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

describe('JobDetailsComponent', () => {
  let fixture:ComponentFixture<JobDetailsComponent>;
  let component:JobDetailsComponent;
  let data:IJob[] = [
    {id:1,jobName:'job1', jobGroup:'group1',jobClass:'com.fastCode.class1'},
    {id:1,jobName:'job2', jobGroup:'group2',jobClass:'com.fastCode.class2'}
  ];

  let httpTestingController: HttpTestingController;
  let jobService: JobService;
  let baseUrl:string = environment.apiUrl;
  
  beforeEach(async(() => {
    
    TestBed.configureTestingModule({
      declarations: [
        JobDetailsComponent    
      ],
      imports: [TestingModule],
      providers: [
        JobService,     
        ChangeDetectorRef,
      ],
   
    //  schemas: [ NO_ERRORS_SCHEMA ]
    }).compileComponents();
  
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobDetailsComponent);
    httpTestingController = TestBed.get(HttpTestingController);
    jobService = TestBed.get(JobService);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #ngOnInit()', async () => {
    httpTestingController = TestBed.get(HttpTestingController);

    fixture.detectChanges();    
   
    const req = httpTestingController.expectOne(baseUrl + '/jobs/job1/group1').flush(data);  
    //expect(component.users.length).toEqual(2);
    httpTestingController.verify();
  });

  it('should run #OnSubmit()', async () => {
 
    httpTestingController = TestBed.get(HttpTestingController);

    fixture.detectChanges();
   
    const req = httpTestingController.expectOne(baseUrl + '/jobs/job1/group1').flush(data[0]);
    component.onSubmit();  
    const req3 = httpTestingController.expectOne(baseUrl + '/jobs/job1/group1').flush(data[0]);     

    //const result = component.add();    
    httpTestingController.verify();

  
  });
});
