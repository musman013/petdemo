import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecutingJobsComponent } from './executing-jobs.component';

describe('ExecutingJobsComponent', () => {
  let component: ExecutingJobsComponent;
  let fixture: ComponentFixture<ExecutingJobsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExecutingJobsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExecutingJobsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
