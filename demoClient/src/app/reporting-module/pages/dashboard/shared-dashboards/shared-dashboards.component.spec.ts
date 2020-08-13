import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedDashboardsComponent } from './shared-dashboards.component';

describe('SharedDashboardsComponent', () => {
  let component: SharedDashboardsComponent;
  let fixture: ComponentFixture<SharedDashboardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SharedDashboardsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SharedDashboardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
