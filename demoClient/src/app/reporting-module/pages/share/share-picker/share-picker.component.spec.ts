import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharePickerComponent } from './share-picker.component';

describe('SharePickerComponent', () => {
  let component: SharePickerComponent;
  let fixture: ComponentFixture<SharePickerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SharePickerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SharePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
