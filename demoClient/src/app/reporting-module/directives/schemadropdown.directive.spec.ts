import { SchemadropdownDirective } from './schemadropdown.directive';

describe('SchemadropdownDirective', () => {
  it('should create an instance', () => {
    let elRefMock = {
      nativeElement: document.createElement('div')
    };
    const directive = new SchemadropdownDirective(elRefMock);
    expect(directive).toBeTruthy();
  });
});
