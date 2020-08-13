import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedReportsComponent } from './shared-reports.component';

describe('SharedReportsComponent', () => {
  let component: SharedReportsComponent;
  let fixture: ComponentFixture<SharedReportsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SharedReportsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SharedReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
