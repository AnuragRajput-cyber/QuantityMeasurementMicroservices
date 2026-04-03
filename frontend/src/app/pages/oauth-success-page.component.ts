import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AUTH_TOKEN_KEY } from '../core/config/app.constants';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-oauth-success-page',
  templateUrl: './oauth-success-page.component.html',
  styleUrl: './oauth-success-page.component.scss'
})
export class OAuthSuccessPageComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);

  readonly statusMessage = signal('Finishing your Google sign-in...');

  constructor() {
    const token = this.route.snapshot.queryParamMap.get('token') ?? localStorage.getItem(AUTH_TOKEN_KEY);
    const success = this.auth.handleOAuthToken(token);

    this.auth.refreshFromStorage();

    this.statusMessage.set(
      success ? 'Secure session ready. Taking you to the dashboard...' : 'No token was found. Returning to login.'
    );

    window.setTimeout(() => {
      void this.router.navigate([success ? '/dashboard' : '/auth/login'], { replaceUrl: true });
    }, 1200);
  }
}
