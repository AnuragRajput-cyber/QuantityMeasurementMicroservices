import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { APP_API_BASE } from '../config/app.constants';
import { ConvertRequest, QuantityDto } from '../models/quantity.models';

@Injectable({ providedIn: 'root' })
export class QuantityService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${APP_API_BASE}/api/quantity`;

  add(first: QuantityDto, second: QuantityDto): Observable<QuantityDto> {
    return this.http.post<QuantityDto>(`${this.baseUrl}/add`, [first, second]);
  }

  subtract(first: QuantityDto, second: QuantityDto): Observable<QuantityDto> {
    return this.http.post<QuantityDto>(`${this.baseUrl}/subtract`, [first, second]);
  }

  compare(first: QuantityDto, second: QuantityDto): Observable<boolean> {
    return this.http.post<boolean>(`${this.baseUrl}/compare`, [first, second]);
  }

  divide(first: QuantityDto, second: QuantityDto): Observable<number> {
    return this.http.post<number>(`${this.baseUrl}/divide`, [first, second]);
  }

  convert(request: ConvertRequest): Observable<QuantityDto> {
    return this.http.post<QuantityDto>(`${this.baseUrl}/convert`, request);
  }
}
