import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeDetectorRef } from '@angular/core';
import { HttpTestingController } from '@angular/common/http/testing';

import { JobNewComponent, JobsComponent, ListFiltersComponent, AddFilterFieldComponent, JobService, IJob } from './index';

import { TestingModule } from '../../testing/utils';
import { environment } from '../environment';

fdescribe('JobsComponent', () => {
  let component: JobsComponent;
  let fixture: ComponentFixture<JobsComponent>;
  let jobService: JobService;
  
  let baseUrl:string = environment.apiUrl;

  let data: IJob[] = [
    {
      id: 1,
      jobName: "job1",
      jobGroup: "group1",
      jobClass: "com.fastcode.job1"
    },
    {
      id: 2,
      jobName: "job2",
      jobGroup: "group2",
      jobClass: "com.fastcode.job2"
    }
  ];

  let httpTestingController: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [JobsComponent],
      imports: [TestingModule],
      providers: [
        JobService,
        ChangeDetectorRef,
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JobsComponent);
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
    const req = httpTestingController.expectOne(req => req.method === 'GET' && req.url === baseUrl + '/jobs').flush(data);
    expect(component.jobs.length).toEqual(data.length);
    httpTestingController.verify();
  });

  it('should list Jobs', async () => {
    httpTestingController = TestBed.get(HttpTestingController);
    fixture.detectChanges();
    const req = httpTestingController.expectOne(req => req.method === 'GET' && req.url === baseUrl + '/jobs').flush(data);
    expect(component.jobs.length).toEqual(data.length);
    fixture.detectChanges();
    httpTestingController.verify();
  });

  it('should run #Add()', async () => {
    httpTestingController = TestBed.get(HttpTestingController);

    fixture.detectChanges();
   
    const req = httpTestingController.expectOne(req => req.method === 'GET' && req.url === baseUrl + '/jobs').flush(data);
    const result = component.add(); 
    httpTestingController.verify();
  });
});
