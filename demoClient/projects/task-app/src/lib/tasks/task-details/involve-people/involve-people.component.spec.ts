import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InvolvePeopleComponent } from './involve-people.component';

describe('InvolvePeopleComponent', () => {
  let component: InvolvePeopleComponent;
  let fixture: ComponentFixture<InvolvePeopleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InvolvePeopleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InvolvePeopleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
