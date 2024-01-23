import {Component, forwardRef, Input, OnInit} from '@angular/core';
import {ControlValueAccessor, FormsModule, NG_VALUE_ACCESSOR} from "@angular/forms";
import {NumericInputDirective} from "../../directives/numeric-input.directive";

@Component({
  selector: 'app-spinner',
  standalone: true,
  imports: [
    FormsModule,
    NumericInputDirective
  ],
  templateUrl: './spinner.component.html',
  styleUrl: './spinner.component.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SpinnerComponent),
      multi: true,
    },
  ],
})
export class SpinnerComponent implements OnInit, ControlValueAccessor{
  @Input() min: number = 100
  @Input() max: number = 0
  @Input() step: number = 1
  @Input() name: string = ''
  @Input() id: string = ''
  value: number = 0
  onChange: any = () => {};
  onTouch: any = () => {};

  ngOnInit(): void {
    if (this.max && this.value > this.max){
      this.value = this.max
    }
    if (this.min && this.value < this.min){
      this.value = this.min
    }
  }

  plus(){
    if (this.value + this.step <= this.max) {
      this.value = this.value + this.step
      this.onChange(this.value);
      this.onTouch();
    }
  }
  minus(){
    if (this.value - this.step >= this.min) {
      this.value = this.value - this.step
      this.onChange(this.value);
      this.onTouch();
    }
  }

  onInput(event: any) {
    this.value = event.target.value;
    this.onChange(this.value);
    this.onTouch();
  }

  writeValue(value: any): void {
    if (value !== undefined) {
      this.value = value;
    }
  }
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }
}
