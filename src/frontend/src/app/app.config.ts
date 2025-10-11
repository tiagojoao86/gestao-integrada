import {
  ApplicationConfig,
  inject,
  provideZoneChangeDetection,
  LOCALE_ID,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandlerFn,
  HttpRequest,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';
import { AuthService } from './services/auth-service';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { MessageService } from './components/base/messages/messages.service';
import { messageServiceProvider } from './components/base/messages/message.factory';

registerLocaleData(localePt);

var isRefreshing = false;

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    providePrimeNG({
      theme: {
        preset: Aura,
      },
    }),
    provideHttpClient(withInterceptors([refreshTokenInterceptor])),
    { provide: LOCALE_ID, useValue: 'pt' },
    { provide: MessageService, useFactory: messageServiceProvider },
  ],
};

export function tokenInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {
  if (req.url !== '/api/authenticate' && req.url !== '/authenticate/refresh') {
    let authService: AuthService = inject(AuthService);
    const token = authService.getToken();
    if (token) {
      const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
      return next(authReq);
    }
  }

  return next(req);
}

export function refreshTokenInterceptor(
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> {
  if (
    req.url === '/api/authenticate' ||
    req.url === '/api/authenticate/refresh'
  ) {
    return next(req);
  }

  let authService: AuthService = inject(AuthService);

  const token = authService.getToken();

  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !isRefreshing) {
        isRefreshing = true;
        return authService.refreshToken().pipe(
          switchMap((newToken) => {
            isRefreshing = false;
            return next(
              req.clone({
                setHeaders: { Authorization: `Bearer ${newToken.body.token}` },
              })
            );
          }),
          catchError((e) => {
            console.log(e);
            isRefreshing = false;
            authService.logout();
            return throwError(
              () => new Error('Sessão expirada, faça login novamente')
            );
          })
        );
      }
      return throwError(() => error);
    })
  );
}
