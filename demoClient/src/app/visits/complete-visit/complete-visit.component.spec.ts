import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteVisitComponent } from './complete-visit.component';

describe('CompleteVisitComponent', () => {
  let component: CompleteVisitComponent;
  let fixture: ComponentFixture<CompleteVisitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompleteVisitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompleteVisitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
