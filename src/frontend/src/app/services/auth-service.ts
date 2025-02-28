import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, tap } from 'rxjs';
import { Response } from '../model/response';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private tokenSubject = new BehaviorSubject<string | null>(this.getToken());
  private username = new BehaviorSubject<string | null>(this.getToken());
  private name = new BehaviorSubject<string | null>(this.getToken());
  token$ = this.tokenSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    return this.http
      .post<Response>('/api/authenticate', { username, password })
      .pipe(
        tap((response) => {
          this.setToken(response.body.token);
          this.setUsername(response.body.username);
          this.setName(response.body.name);
          this.router.navigate(['/']);
        }),
        catchError((e) => {
          throw new Error('Error');
        })
      );
  }

  logout() {
    this.clearToken();
    this.router.navigate(['/login']);
  }

  refreshToken() {
    return this.http
      .post<Response>(
        '/api/authenticate/refresh',
        {},
        { withCredentials: true }
      )
      .pipe(
        tap((response) => {
          this.setToken(response.body.token);
          this.setUsername(response.body.username);
          this.setName(response.body.name);
        }),
        catchError(() => {
          this.logout();
          throw new Error('Session expired');
        })
      );
  }

  getUsername(): string | null {
    return sessionStorage.getItem('username');
  }

  getName(): string | null {
    return sessionStorage.getItem('name');
  }

  setUsername(username: string) {
    sessionStorage.setItem('username', username);
    this.username.next(username);
  }

  setName(name: string) {
    sessionStorage.setItem('name', name);
    this.name.next(name);
  }

  getToken(): string | null {
    return sessionStorage.getItem('accessToken');
  }

  setToken(token: string) {
    sessionStorage.setItem('accessToken', token);
    this.tokenSubject.next(token);
  }

  clearToken() {
    sessionStorage.removeItem('accessToken');
    this.tokenSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
