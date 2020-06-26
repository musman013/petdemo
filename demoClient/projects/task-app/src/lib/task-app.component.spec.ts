import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskAppComponent } from './task-app.component';

describe('TaskAppComponent', () => {
  let component: TaskAppComponent;
  let fixture: ComponentFixture<TaskAppComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskAppComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskAppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
