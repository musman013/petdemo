import { TestBed, inject } from '@angular/core/testing';

import { FunctionalGroupService } from './functional-group.service';

describe('FunctionalGroupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FunctionalGroupService]
    });
  });

  it('should be created', inject([FunctionalGroupService], (service: FunctionalGroupService) => {
    expect(service).toBeTruthy();
  }));
});
