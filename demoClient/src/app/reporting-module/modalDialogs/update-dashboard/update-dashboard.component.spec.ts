import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDashboardComponent } from './update-dashboard.component';

describe('UpdateDashboardComponent', () => {
  let component: UpdateDashboardComponent;
  let fixture: ComponentFixture<UpdateDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UpdateDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
