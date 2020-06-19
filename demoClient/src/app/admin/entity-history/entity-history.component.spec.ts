import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangeDetectorRef } from '@angular/core';
import { HttpTestingController } from '@angular/common/http/testing';

import { EntityHistoryComponent } from './entity-history.component';
import { EntityHistoryService } from './entity-history.service';
import { EntityHistory } from './entityHistory'
import { TestingModule } from '../../testing/utils';
import { environment } from '../../environments/environment';
import { ListFiltersComponent } from 'fastCodeCore';
import { AddFilterFieldComponent } from 'fastCodeCore';

describe('EntityHistoryComponent', () => {
  let component: EntityHistoryComponent;
  let fixture: ComponentFixture<EntityHistoryComponent>;
  let httpTestingController: HttpTestingController;
  let entityHistoryService: EntityHistoryService;
  let baseUrl:string = environment.apiUrl;
  let data:EntityHistory[]=[
    {
      "changeType": "SetChange",
      "globalId": {
        "entity": "com.nfinity.fastcode.domain.Authorization.users.UsersEntity",
        "cdoId": 3
      },
      "commitMetadata": {
        "author": "unauthenticated",
        "properties": [],
        "commitDate": "2018-12-05T11:49:31.869",
        "id": 142.00
      },
      "property": "permissions"
    },
    {
      "changeType": "SetChange",
      "globalId": {
        "entity": "com.nfinity.fastcode.domain.Authorization.users.UsersEntity",
        "cdoId": 2
      },
      "commitMetadata": {
        "author": "unauthenticated",
        "properties": [],
        "commitDate": "2018-12-05T11:49:31.804",
        "id": 141.00
      },
      "property": "permissions"
    }
  ]

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EntityHistoryComponent, ListFiltersComponent, AddFilterFieldComponent],
      imports: [TestingModule],
      providers: [
        EntityHistoryService,
        ChangeDetectorRef,
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityHistoryComponent);
    httpTestingController = TestBed.get(HttpTestingController);
    entityHistoryService = TestBed.get(EntityHistoryService);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #ngOnInit()', async () => {
    httpTestingController = TestBed.get(HttpTestingController);
    fixture.detectChanges();
    const req = httpTestingController.expectOne(req => req.method === 'GET' && req.url === baseUrl + '/audit/changes').flush(data);
    expect(component.entityHistory.length).toEqual(data.length);
    httpTestingController.verify();
  });

  it('should list entity history', async () => {
    httpTestingController = TestBed.get(HttpTestingController);
    fixture.detectChanges();
    const req = httpTestingController.expectOne(req => req.method === 'GET' && req.url === baseUrl + '/audit/changes').flush(data);
    expect(component.entityHistory.length).toEqual(data.length);
    fixture.detectChanges();
    httpTestingController.verify();
  });
});
