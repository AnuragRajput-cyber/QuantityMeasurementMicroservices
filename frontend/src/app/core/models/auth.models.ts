export interface AuthResponse {
  token: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface UserProfile {
  email: string;
  name: string;
  picture: string | null;
  initials: string;
  provider: 'LOCAL' | 'GOOGLE' | 'UNKNOWN';
}
