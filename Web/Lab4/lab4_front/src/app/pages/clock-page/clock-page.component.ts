import {Component} from '@angular/core';
import {NgIf} from "@angular/common";
import {UserService} from "../../services/user.service";
import {ClockComponent} from "../../components/clock/clock.component";
import {environment} from "../../../environments/environment";
import {Router} from "@angular/router";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
  selector: 'app-clock-page',
  standalone: true,
  imports: [NgIf, ClockComponent, ReactiveFormsModule],
  templateUrl: './clock-page.component.html',
  styleUrl: './clock-page.component.css'
})
export class ClockPageComponent {
  constructor(private router: Router, public userService: UserService) {
  }

  formLogin = new FormGroup({
    login: new FormControl<string>('', [Validators.required]),
    password: new FormControl<string>('', [Validators.required])
  })

  login() {
    this.userService.user = {
      login: this.formLogin.value.login as string,
      password: this.formLogin.value.password as string
    }

    const url = environment.backendURL + '/users/login';
    this.userService.proceedAuthRequest(url)
      .subscribe({
        next: ((res: string) => {
          localStorage.setItem('authToken', res);
          if (this.userService.user)
            localStorage.setItem('login', this.userService.user.login);
          this.router.navigate(['main']).then(r => {
            if (!r) {
              console.error("something went wrong...");
            }
          });
        })
      });
  }

  formRegistration = new FormGroup({
    login: new FormControl<string>('', []),
    password1: new FormControl<string>('', [
      Validators.required,
      Validators.minLength(6)
    ]),
    password2: new FormControl<string>('', [
      Validators.required,
      Validators.minLength(6),
    ])
  })
  get RegPassword1() {
    return this.formRegistration.controls.password1 as FormControl
  }
  get RegPassword2() {
    return this.formRegistration.controls.password2 as FormControl
  }

  registration() {
    if (this.formRegistration.value.password1 != this.formRegistration.value.password2) {
      return;
    }

    this.userService.user = {
      login: this.formRegistration.value.login as string,
      password: this.formRegistration.value.password1 as string
    }

    const url = environment.backendURL + '/users/registration';
    this.userService.proceedAuthRequest(url)
      .subscribe({
        next: ((res: string) => {
          localStorage.setItem('authToken', res);
          if (this.userService.user)
            localStorage.setItem('login', this.userService.user.login);
          this.router.navigate(['main']).then(r => {
            if (!r) {
              console.error("something went wrong...");
            }
          });
        })
      });
  }
}
