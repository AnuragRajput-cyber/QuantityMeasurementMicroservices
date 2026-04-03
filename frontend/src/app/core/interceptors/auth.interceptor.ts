import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { APP_API_BASE } from '../config/app.constants';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const auth = inject(AuthService);

  const isGatewayRequest = request.url.startsWith(APP_API_BASE);
  const isPublicAuthRequest =
    request.url.includes('/auth/login') ||
    request.url.includes('/auth/register') ||
    request.url.includes('/oauth2/') ||
    request.url.includes('/login/oauth2') ||
    request.url.includes('/v3/api-docs');

  if (!isGatewayRequest || isPublicAuthRequest) {
    return next(request);
  }

  const token = auth.getAuthToken();
  if (!token) {
    return next(request);
  }

  const authenticatedRequest = request.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });

  return next(authenticatedRequest).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        auth.logout(false);
      }

      return throwError(() => error);
    })
  );
};
