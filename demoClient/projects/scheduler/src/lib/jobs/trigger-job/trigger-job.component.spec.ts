import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TriggerJobComponent } from './trigger-job.component';

describe('TriggerJobComponent', () => {
  let component: TriggerJobComponent;
  let fixture: ComponentFixture<TriggerJobComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TriggerJobComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TriggerJobComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
