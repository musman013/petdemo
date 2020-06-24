import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CalculateFieldComponent } from './calculate-field.component';

describe('CalculateFieldComponent', () => {
  let component: CalculateFieldComponent;
  let fixture: ComponentFixture<CalculateFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CalculateFieldComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CalculateFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
