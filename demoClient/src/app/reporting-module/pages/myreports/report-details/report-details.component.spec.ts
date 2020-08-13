import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyreportsDetailComponent } from './report-details.component';

describe('MyreportsDetailComponent', () => {
  let component: MyreportsDetailComponent;
  let fixture: ComponentFixture<MyreportsDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyreportsDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyreportsDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
