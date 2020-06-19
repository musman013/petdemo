import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { schedulerComponent } from './scheduler.component';

describe('schedulerComponent', () => {
  let component: schedulerComponent;
  let fixture: ComponentFixture<schedulerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ schedulerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(schedulerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
