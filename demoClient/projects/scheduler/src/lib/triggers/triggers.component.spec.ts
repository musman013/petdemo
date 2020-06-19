import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeDetectorRef } from '@angular/core';
import { HttpTestingController } from '@angular/common/http/testing';

import { TriggerNewComponent, TriggersComponent, ListFiltersComponent, AddFilterFieldComponent, TriggerService, ITrigger } from './index';

import { TestingModule } from '../../testing/utils';
import { environment } from '../environment';

fdescribe('TriggersComponent', () => {
  let component: TriggersComponent;
  let fixture: ComponentFixture<TriggersComponent>;
  let triggerService: TriggerService;
  
  let baseUrl:string = environment.apiUrl;

  let data: ITrigger[] = [
    {
      id: 1,
      triggerName: "trigger1",
      triggerGroup: "group1",
      jobName: "job1",
      jobGroup: "group1",
      triggerType: "Simple"
    },
    {
      id: 2,
      triggerName: "trigger2",
      triggerGroup: "group1",
      jobName: "job2",
      jobGroup: "group1",
      triggerType: "Cron"
    }
  ];

  let httpTestingController: HttpTestingController;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TriggersComponent],
      imports: [TestingModule],
      providers: [
        TriggerService,
        ChangeDetectorRef,
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TriggersComponent);
    httpTestingController = TestBed.get(HttpTestingController);
    triggerService = TestBed.get(TriggerService);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #ngOnInit()', async () => {
    httpTestingController = TestBed.get(HttpTestingController);
    fixture.detectChanges();
    const req = httpTestingController.expectOne(req => req.method === 'GET' && req.url === baseUrl + '/triggers').flush(data);
    expect(component.triggers.length).toEqual(data.length);
    httpTestingController.verify();
  });

  it('should list Triggers', async () => {
    httpTestingController = TestBed.get(HttpTestingController);
    fixture.detectChanges();
    const req = httpTestingController.expectOne(req => req.method === 'GET' && req.url === baseUrl + '/triggers').flush(data);
    expect(component.triggers.length).toEqual(data.length);
    fixture.detectChanges();
    httpTestingController.verify();
  });

  it('should run #Add()', async () => {
    httpTestingController = TestBed.get(HttpTestingController);

    fixture.detectChanges();
   
    const req = httpTestingController.expectOne(req => req.method === 'GET' && req.url === baseUrl + '/triggers').flush(data);
    const result = component.add(); 
    httpTestingController.verify();
  });
});
