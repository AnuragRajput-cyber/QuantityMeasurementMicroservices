import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-login-page',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {
  protected readonly auth = inject(AuthService);

  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);

  readonly errorMessage = signal('');

  readonly form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.errorMessage.set('');

    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => void this.router.navigate(['/dashboard']),
      error: (error) => this.errorMessage.set(this.extractMessage(error))
    });
  }

  loginWithGoogle(): void {
    this.auth.oauthLogin();
  }

  private extractMessage(error: unknown): string {
    const fallback = 'We could not sign you in. Please check your credentials and try again.';
    if (!error || typeof error !== 'object') {
      return fallback;
    }

    const backendMessage = (error as { error?: { error?: string; message?: string } }).error;
    return backendMessage?.error || backendMessage?.message || fallback;
  }
}
