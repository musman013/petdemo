import { TestBed, inject } from '@angular/core/testing';

import { schedulerService } from './scheduler.service';

describe('schedulerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [schedulerService]
    });
  });

  it('should be created', inject([schedulerService], (service: schedulerService) => {
    expect(service).toBeTruthy();
  }));
});
