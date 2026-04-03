import { CommonModule } from '@angular/common';
import { Component, computed, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { AuthService } from '../core/services/auth.service';
import {
  OPERATION_CARDS,
  QuantityCategory,
  QuantityDto,
  QuantityHistoryEntry,
  QuantityOperation,
  UNITS_BY_CATEGORY
} from '../core/models/quantity.models';
import { QuantityHistoryService } from '../core/services/quantity-history.service';
import { QuantityService } from '../core/services/quantity.service';

interface ResultCard {
  label: string;
  detail: string;
  tone: 'neutral' | 'success' | 'warning';
}

@Component({
  selector: 'app-dashboard-page',
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent {
  protected readonly auth = inject(AuthService);

  private readonly historyService = inject(QuantityHistoryService);
  private readonly quantityService = inject(QuantityService);

  readonly operationCards = OPERATION_CARDS;
  readonly categories = Object.keys(UNITS_BY_CATEGORY) as QuantityCategory[];

  readonly selectedOperation = signal<QuantityOperation>('convert');
  readonly selectedCategory = signal<QuantityCategory>('length');
  readonly leftValue = signal(1);
  readonly rightValue = signal(100);
  readonly leftUnit = signal('METER');
  readonly rightUnit = signal('CENTIMETER');
  readonly isBusy = signal(false);
  readonly errorMessage = signal('');
  readonly result = signal<ResultCard | null>(null);
  readonly showHistory = signal(false);
  readonly history = signal<QuantityHistoryEntry[]>([]);

  readonly unitOptions = computed(() => UNITS_BY_CATEGORY[this.selectedCategory()]);
  readonly isConvertOperation = computed(() => this.selectedOperation() === 'convert');
  readonly selectedOperationMeta = computed(
    () => this.operationCards.find((card) => card.id === this.selectedOperation()) ?? this.operationCards[0]
  );
  readonly temperatureBlocked = computed(() => {
    const operation = this.selectedOperation();
    return this.selectedCategory() === 'temperature' && ['add', 'subtract', 'divide'].includes(operation);
  });

  constructor() {
    effect(() => {
      const units = this.unitOptions();

      if (!units.includes(this.leftUnit())) {
        this.leftUnit.set(units[0]);
      }

      if (!units.includes(this.rightUnit())) {
        this.rightUnit.set(units[Math.min(1, units.length - 1)]);
      }
    });

    effect(() => {
      const email = this.auth.profile()?.email;
      this.history.set(this.historyService.getHistory(email));
    });
  }

  selectOperation(operation: QuantityOperation): void {
    this.selectedOperation.set(operation);
    this.errorMessage.set('');
    this.result.set(null);
  }

  selectCategory(category: QuantityCategory): void {
    this.selectedCategory.set(category);
    this.errorMessage.set('');
    this.result.set(null);
  }

  updateLeftValue(value: string | number): void {
    this.leftValue.set(this.toNumericValue(value, this.leftValue()));
  }

  updateRightValue(value: string | number): void {
    this.rightValue.set(this.toNumericValue(value, this.rightValue()));
  }

  toggleHistory(): void {
    this.showHistory.update((current) => !current);
  }

  clearHistory(): void {
    this.historyService.clearHistory(this.auth.profile()?.email);
    this.history.set([]);
  }

  formatTimestamp(value: string): string {
    return new Date(value).toLocaleString();
  }

  runOperation(): void {
    if (this.temperatureBlocked()) {
      return;
    }

    const operation = this.selectedOperation();
    const first: QuantityDto = { value: Number(this.leftValue()), unit: this.leftUnit() };
    const second: QuantityDto = { value: Number(this.rightValue()), unit: this.rightUnit() };

    this.isBusy.set(true);
    this.errorMessage.set('');
    this.result.set(null);

    const request: Observable<boolean | number | QuantityDto> =
      operation === 'add'
        ? this.quantityService.add(first, second)
        : operation === 'subtract'
          ? this.quantityService.subtract(first, second)
          : operation === 'compare'
            ? this.quantityService.compare(first, second)
            : operation === 'divide'
              ? this.quantityService.divide(first, second)
              : this.quantityService.convert({
                  value: Number(this.leftValue()),
                  fromUnit: this.leftUnit(),
                  toUnit: this.rightUnit()
                });

    request.subscribe({
      next: (response: boolean | number | QuantityDto) => {
        let nextResult: ResultCard;

        if (typeof response === 'boolean') {
          nextResult = {
            label: response ? 'Equivalent quantities' : 'Not equivalent',
            detail: response
              ? 'Both inputs resolve to the same real measurement.'
              : 'The backend reports these measurements as different.',
            tone: response ? 'success' : 'warning'
          };
        } else if (typeof response === 'number') {
          nextResult = {
            label: 'Division ratio',
            detail: response.toFixed(4),
            tone: 'neutral'
          };
        } else {
          nextResult = {
            label: operation === 'convert' ? 'Converted result' : 'Computed result',
            detail: `${Number(response.value).toFixed(4)} ${response.unit}`,
            tone: 'success'
          };
        }

        this.result.set(nextResult);
        this.history.set(
          this.historyService.addEntry(this.auth.profile()?.email, this.buildHistoryEntry(nextResult))
        );
        this.isBusy.set(false);
      },
      error: (error: { error?: { error?: string; message?: string } }) => {
        const backendError = error?.error?.error || error?.error?.message;
        this.errorMessage.set(backendError || 'The service could not complete that quantity operation.');
        this.isBusy.set(false);
      }
    });
  }

  private toNumericValue(value: string | number, fallback: number): number {
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : fallback;
  }

  private buildHistoryEntry(result: ResultCard): QuantityHistoryEntry {
    const operation = this.selectedOperation();
    const category = this.selectedCategory();
    const summary = operation === 'convert'
      ? `${this.leftValue()} ${this.leftUnit()} to ${this.rightUnit()}`
      : `${this.leftValue()} ${this.leftUnit()} and ${this.rightValue()} ${this.rightUnit()}`;

    return {
      id: `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`,
      operation,
      category,
      summary,
      resultLabel: result.label,
      resultDetail: result.detail,
      createdAt: new Date().toISOString()
    };
  }
}
