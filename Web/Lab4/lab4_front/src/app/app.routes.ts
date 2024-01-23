import { Routes } from '@angular/router';
import {ClockPageComponent} from "./pages/clock-page/clock-page.component";
import {MainPageComponent} from "./pages/main-page/main-page.component";
import {AuthenticationGuard} from "./authentication.guard";

export const routes: Routes = [
  {
    path: '',
    canActivate: [AuthenticationGuard], children: [
      {
        path: '',
        component: ClockPageComponent
      },
      {
        path: 'main',
        component: MainPageComponent
      },
      {
        path: '**',
        redirectTo: ''
      }
    ]
  }
];
