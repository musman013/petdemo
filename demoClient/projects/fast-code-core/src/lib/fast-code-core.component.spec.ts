import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FastCodeCoreComponent } from './fast-code-core.component';

describe('FastCodeCoreComponent', () => {
  let component: FastCodeCoreComponent;
  let fixture: ComponentFixture<FastCodeCoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FastCodeCoreComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FastCodeCoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
