import { inject, Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from './auth-service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private authService: AuthService = inject(AuthService); 

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const token = this.authService.getToken();

    let authReq = req;
    if (token) {
      authReq = req.clone({
        setHeaders: { Authorization: token },
      });
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !this.isRefreshing) {
          this.isRefreshing = true;
          return this.authService.refreshToken().pipe(
            switchMap((newToken) => {
              this.isRefreshing = false;
              return next.handle(
                req.clone({
                  setHeaders: { Authorization: newToken.body.token },
                })
              );
            }),
            catchError(() => {
              this.isRefreshing = false;
              this.authService.logout();
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
}
