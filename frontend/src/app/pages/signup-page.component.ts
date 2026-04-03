import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

function passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
  const password = control.get('password')?.value;
  const confirmPassword = control.get('confirmPassword')?.value;

  return password === confirmPassword ? null : { passwordMismatch: true };
}

@Component({
  selector: 'app-signup-page',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './signup-page.component.html',
  styleUrl: './signup-page.component.scss'
})
export class SignupPageComponent {
  protected readonly auth = inject(AuthService);

  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);

  readonly errorMessage = signal('');

  readonly form = this.fb.nonNullable.group(
    {
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    },
    { validators: passwordMatchValidator }
  );

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { name, email, password } = this.form.getRawValue();
    this.errorMessage.set('');

    this.auth.register({ name, email, password }).subscribe({
      next: () => void this.router.navigate(['/dashboard']),
      error: (error) => this.errorMessage.set(this.extractMessage(error))
    });
  }

  private extractMessage(error: unknown): string {
    const fallback = 'We could not create your account. Please verify your details and try again.';
    if (!error || typeof error !== 'object') {
      return fallback;
    }

    const backendMessage = (error as { error?: { error?: string; message?: string } }).error;
    return backendMessage?.error || backendMessage?.message || fallback;
  }
}
