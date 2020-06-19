import { TestBed, inject } from '@angular/core/testing';

import { FastCodeCoreService } from './fast-code-core.service';

describe('FastCodeCoreService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FastCodeCoreService]
    });
  });

  it('should be created', inject([FastCodeCoreService], (service: FastCodeCoreService) => {
    expect(service).toBeTruthy();
  }));
});
