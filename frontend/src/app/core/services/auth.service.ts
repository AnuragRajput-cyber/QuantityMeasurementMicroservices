import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, finalize, map, tap } from 'rxjs';
import { APP_API_BASE, AUTH_TOKEN_KEY } from '../config/app.constants';
import { AuthResponse, LoginRequest, RegisterRequest, UserProfile } from '../models/auth.models';

interface JwtPayload {
  sub?: string;
  name?: string;
  picture?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private readonly tokenState = signal<string | null>(this.readStoredToken());
  private readonly profileState = signal<UserProfile | null>(this.buildProfile(this.tokenState()));
  private readonly busyState = signal(false);

  readonly token = computed(() => this.tokenState());
  readonly profile = computed(() => this.profileState());
  readonly busy = computed(() => this.busyState());
  readonly isAuthenticated = computed(() => !!this.profileState());

  login(payload: LoginRequest): Observable<void> {
    this.busyState.set(true);

    return this.http.post<AuthResponse>(`${APP_API_BASE}/auth/login`, payload).pipe(
      tap((response) => this.setSession(response.token)),
      map(() => void 0),
      finalize(() => this.busyState.set(false))
    );
  }

  register(payload: RegisterRequest): Observable<void> {
    this.busyState.set(true);

    return this.http.post<AuthResponse>(`${APP_API_BASE}/auth/register`, payload).pipe(
      tap((response) => this.setSession(response.token)),
      map(() => void 0),
      finalize(() => this.busyState.set(false))
    );
  }

  oauthLogin(): void {
    window.location.href = `${APP_API_BASE}/oauth2/authorization/google`;
  }

  handleOAuthToken(token: string | null): boolean {
    if (!token) {
      return false;
    }

    this.setSession(token);
    return true;
  }

  refreshFromStorage(): void {
    const token = this.readStoredToken();
    this.tokenState.set(token);
    this.profileState.set(this.buildProfile(token));
  }

  getAuthToken(): string | null {
    return this.tokenState();
  }

  logout(redirect = true): void {
    localStorage.removeItem(AUTH_TOKEN_KEY);
    this.tokenState.set(null);
    this.profileState.set(null);

    if (redirect) {
      void this.router.navigate(['/auth/login']);
    }
  }

  private setSession(token: string): void {
    localStorage.setItem(AUTH_TOKEN_KEY, token);
    this.tokenState.set(token);
    this.profileState.set(this.buildProfile(token));
  }

  private buildProfile(token: string | null): UserProfile | null {
    if (!token) {
      return null;
    }

    const payload = this.decodeToken(token);
    if (!payload?.sub) {
      return null;
    }

    const email = payload.sub;
    const name = payload.name?.trim() || this.fallbackName(email);
    const picture = payload.picture?.trim() || null;

    return {
      email,
      name,
      picture,
      initials: this.makeInitials(name, email),
      provider: picture ? 'GOOGLE' : 'LOCAL'
    };
  }

  private decodeToken(token: string): JwtPayload | null {
    try {
      const payload = token.split('.')[1];
      if (!payload) {
        return null;
      }

      const normalized = payload.replace(/-/g, '+').replace(/_/g, '/');
      const padded = normalized.padEnd(normalized.length + ((4 - normalized.length % 4) % 4), '=');
      return JSON.parse(window.atob(padded)) as JwtPayload;
    } catch {
      return null;
    }
  }

  private fallbackName(email: string): string {
    return email
      .split('@')[0]
      .split(/[._-]/)
      .filter(Boolean)
      .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
      .join(' ');
  }

  private makeInitials(name: string, email: string): string {
    const parts = name.split(' ').filter(Boolean);
    if (parts.length >= 2) {
      return `${parts[0][0]}${parts[1][0]}`.toUpperCase();
    }

    const base = parts[0] || email;
    return base.slice(0, 2).toUpperCase();
  }

  private readStoredToken(): string | null {
    return typeof window === 'undefined' ? null : localStorage.getItem(AUTH_TOKEN_KEY);
  }
}
