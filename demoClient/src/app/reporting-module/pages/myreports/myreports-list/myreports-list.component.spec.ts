import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyreportsListComponent } from './myreports-list.component';

describe('MyreportsListComponent', () => {
  let component: MyreportsListComponent;
  let fixture: ComponentFixture<MyreportsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyreportsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyreportsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
