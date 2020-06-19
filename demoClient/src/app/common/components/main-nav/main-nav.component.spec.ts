
import { fakeAsync, ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSidenavModule } from '@angular/material/sidenav';

import { MatExpansionModule } from '@angular/material/expansion';
import { MainNavComponent } from './main-nav.component';
import { TestingModule, EntryComponents } from 'src/testing/utils';
import { BottomTabNavComponent } from '../bottom-tab-nav/bottom-tab-nav.component';

describe('MainNavComponent', () => {
  let component: MainNavComponent;
  let fixture: ComponentFixture<MainNavComponent>;

  beforeEach(fakeAsync(() => {
    TestBed.configureTestingModule({
      imports: [TestingModule, MatSidenavModule, MatExpansionModule],
      declarations: [MainNavComponent, BottomTabNavComponent].concat(EntryComponents)
    })
    .compileComponents();

    fixture = TestBed.createComponent(MainNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should compile', () => {
    expect(component).toBeTruthy();
  });
});
