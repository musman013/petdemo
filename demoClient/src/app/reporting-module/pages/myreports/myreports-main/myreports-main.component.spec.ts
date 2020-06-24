import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyreportsMainComponent } from './myreports-main.component';

describe('MyreportsMainComponent', () => {
  let component: MyreportsMainComponent;
  let fixture: ComponentFixture<MyreportsMainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyreportsMainComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyreportsMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
