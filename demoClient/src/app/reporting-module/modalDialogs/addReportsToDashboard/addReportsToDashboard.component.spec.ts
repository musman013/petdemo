import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReportsToDashboardComponent } from './addReportsToDashboard.component';

describe('AddReportsToDashboardComponent', () => {
  let component: AddReportsToDashboardComponent;
  let fixture: ComponentFixture<AddReportsToDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddReportsToDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddReportsToDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
