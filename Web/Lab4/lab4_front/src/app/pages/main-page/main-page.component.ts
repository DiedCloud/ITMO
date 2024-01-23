import {
  Component,
  OnInit,
  ViewChild,
  AfterViewChecked
} from '@angular/core';
import {PlaneComponent} from "../../components/plane/plane.component";
import {PointsTableComponent} from "../../components/points-table/points-table.component";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {PointsService} from "../../services/points.service";
import {UserService} from "../../services/user.service";
import {AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {SpinnerComponent} from "../../components/spinner/spinner.component";
import {NgIf} from "@angular/common";
import {NumericInputDirective} from "../../directives/numeric-input.directive";

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    PlaneComponent,
    PointsTableComponent,
    ReactiveFormsModule,
    SpinnerComponent,
    NgIf,
    FormsModule,
    NumericInputDirective
  ],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})
export class MainPageComponent implements OnInit, AfterViewChecked {
  @ViewChild(PlaneComponent) appplane: PlaneComponent;

  constructor(private router: Router,
              private http: HttpClient,
              private pointsService: PointsService,
              private userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.pointsService.getAll().subscribe({
        error: (err) => {
          console.error(err);
          console.log(err.message);
        }
      }
    )
  }

  ngAfterViewChecked(): void {
    this.appplane.drawFieldsByR(this.form.value.r as number);
  }

  form = new FormGroup({
    r: new FormControl<number>(0, [
      Validators.required,
      Validators.min(-5),
      Validators.max(3)
    ]),
    x: new FormControl<number>(0, [
      Validators.required,
      Validators.min(-3),
      Validators.max(5)
    ]),
    y: new FormControl<string>('', [
      Validators.required,
      (control: AbstractControl) => {
        if (control.value.toString().match(/\.[0-9]{15}0*[1-9]/g)) {
          return { invalid: true };
        }
        return null;
      },
      (control: AbstractControl) => {
        const val = parseFloat(control.value)
        if (val < -5 || val > 3) {
          return { invalid: true };
        }
        return null;
      }
    ])
  })

  get r(){
    return this.form.controls.r as FormControl
  }
  get x(){
    return this.form.controls.x as FormControl
  }
  get y(){
    return this.form.controls.y as FormControl
  }

  sendData() {
    const request = {
      r: this.form.value.r as number,
      x: this.form.value.x as number,
      y: this.form.value.y as string
    }
    this.pointsService.create(request).subscribe({
      error: (err) => {
        console.error(err);
        console.log(err.message);
      }
    });
  }

  clearTable() {
    this.pointsService.clear().subscribe({
        error: (err) => {
          console.error(err);
          console.log(err.message);
        }
      }
    );
  }

  logout() {
    this.userService.logout().subscribe({
      next: () => {
        localStorage.clear();
        this.userService.user = null
        this.router.navigate(['']).then(r => {
          if (!r) {
            console.error("something went wrong...");
          }
        });
      }
    });
  }
}
