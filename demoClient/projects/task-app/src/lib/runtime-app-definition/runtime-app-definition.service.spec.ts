import { TestBed, inject } from '@angular/core/testing';

import { RuntimeAppDefinitionService } from './runtime-app-definition.service';

describe('RuntimeAppDefinitionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RuntimeAppDefinitionService]
    });
  });

  it('should be created', inject([RuntimeAppDefinitionService], (service: RuntimeAppDefinitionService) => {
    expect(service).toBeTruthy();
  }));
});
