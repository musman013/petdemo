import { TestBed, inject } from '@angular/core/testing';

import { TaskAppService } from './task-app.service';

describe('TaskAppService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TaskAppService]
    });
  });

  it('should be created', inject([TaskAppService], (service: TaskAppService) => {
    expect(service).toBeTruthy();
  }));
});
