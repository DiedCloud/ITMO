import {Directive, ElementRef, HostListener} from '@angular/core';

@Directive({
  selector: '[appNumericInputDirective]',
  standalone: true
})
export class NumericInputDirective {
  private regex: RegExp = new RegExp(/^[0-9]*\.?[0-9]*$/);

  constructor(private el: ElementRef) {}

  @HostListener('input', ['$event'])
  onInput(event: any) {
    const inputVal: string = event.target.value;
    if (!this.regex.test(inputVal)) {
      event.target.value = inputVal.replace(/[^0-9\.]/g, '');
    }
  }
}
