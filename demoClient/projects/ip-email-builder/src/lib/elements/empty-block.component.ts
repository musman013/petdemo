import { Component } from '@angular/core';

@Component({
  selector: 'ip-empty-block',
  template: `
    <p>Drag some blocks here.</p>
  `,
  styles: [
    `
      :host {
        display: block;
        width: 100%;
        padding: 1rem;
        color: gray;
      }
    `
  ]
})
export class EmptyBlockComponent {
  constructor() {}
}
