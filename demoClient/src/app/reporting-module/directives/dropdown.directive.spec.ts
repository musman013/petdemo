import { DropdownDirective } from './dropdown.directive';

describe('DropdownDirective', () => {
  it('should create an instance', () => {
    let elRefMock = {
      nativeElement: document.createElement('div')
    };
    const directive = new DropdownDirective(elRefMock);
    expect(directive).toBeTruthy();
  });
});
