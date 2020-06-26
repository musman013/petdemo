import { Directive, ElementRef, Renderer, HostListener } from '@angular/core';
@Directive({ selector: '[myActive]' })
export class ActiveDirective {

    private _isActive = false;

    constructor(private el: ElementRef, private renderer: Renderer) {
        console.log(this.el);
    }

    @HostListener('click', ['$event'])
    onClick(e) {
        e.preventDefault();
        this._isActive = !this._isActive;
        this.renderer.setElementClass(this.el.nativeElement, 'active', this._isActive);
    }
}