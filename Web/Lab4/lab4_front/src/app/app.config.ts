import {ApplicationConfig} from '@angular/core';
import {provideRouter, Router} from '@angular/router';

import {routes} from './app.routes';
import {provideClientHydration} from '@angular/platform-browser';
import {
  HttpEvent,
  HttpHandlerFn,
  HttpRequest,
  provideHttpClient,
  withFetch,
  withInterceptors
} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {DatePipe} from "@angular/common";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(
      withFetch(),
      withInterceptors([
        (request: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
          let authToken = localStorage.getItem('authToken');
          if (authToken) {
            request = request.clone({ headers: request.headers.set('Authorization', authToken) });
          }
          return next(request).pipe(
            catchError((error) => {
              if (error.status === 401) {
                localStorage.clear();
                alert("Данные авторизации устарели. Обновите страницу и войди в аккаунт заново.");
              }
              return throwError(error);
            }));
        }
      ])
    ),
    DatePipe
  ]
};
