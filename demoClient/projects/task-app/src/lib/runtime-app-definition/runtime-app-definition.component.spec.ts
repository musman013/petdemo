import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RuntimeAppDefinitionComponent } from './runtime-app-definition.component';

describe('RuntimeAppDefinitionComponent', () => {
  let component: RuntimeAppDefinitionComponent;
  let fixture: ComponentFixture<RuntimeAppDefinitionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RuntimeAppDefinitionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RuntimeAppDefinitionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
