import { Injectable } from '@angular/core';
import { QuantityHistoryEntry } from '../models/quantity.models';

const HISTORY_LIMIT = 12;
const HISTORY_PREFIX = 'qma_history_';

@Injectable({ providedIn: 'root' })
export class QuantityHistoryService {
  getHistory(email: string | null | undefined): QuantityHistoryEntry[] {
    const key = this.buildKey(email);
    if (!key || typeof window === 'undefined') {
      return [];
    }

    try {
      const raw = localStorage.getItem(key);
      if (!raw) {
        return [];
      }

      const parsed = JSON.parse(raw) as QuantityHistoryEntry[];
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      return [];
    }
  }

  addEntry(email: string | null | undefined, entry: QuantityHistoryEntry): QuantityHistoryEntry[] {
    const key = this.buildKey(email);
    if (!key || typeof window === 'undefined') {
      return [];
    }

    const nextHistory = [entry, ...this.getHistory(email)].slice(0, HISTORY_LIMIT);
    localStorage.setItem(key, JSON.stringify(nextHistory));
    return nextHistory;
  }

  clearHistory(email: string | null | undefined): void {
    const key = this.buildKey(email);
    if (!key || typeof window === 'undefined') {
      return;
    }

    localStorage.removeItem(key);
  }

  private buildKey(email: string | null | undefined): string | null {
    const normalized = email?.trim().toLowerCase();
    return normalized ? `${HISTORY_PREFIX}${normalized}` : null;
  }
}
