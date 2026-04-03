export type QuantityCategory = 'length' | 'weight' | 'volume' | 'temperature';
export type QuantityOperation = 'convert' | 'compare' | 'add' | 'subtract' | 'divide';

export interface QuantityDto {
  value: number;
  unit: string;
}

export interface ConvertRequest {
  value: number;
  fromUnit: string;
  toUnit: string;
}

export interface OperationCard {
  id: QuantityOperation;
  title: string;
  description: string;
  accent: string;
}

export interface QuantityHistoryEntry {
  id: string;
  operation: QuantityOperation;
  category: QuantityCategory;
  summary: string;
  resultLabel: string;
  resultDetail: string;
  createdAt: string;
}

export const UNITS_BY_CATEGORY: Record<QuantityCategory, readonly string[]> = {
  length: ['METER', 'CENTIMETER', 'MILLIMETER', 'KILOMETER', 'INCH', 'FEET', 'YARD'],
  weight: ['KILOGRAM', 'GRAM', 'POUND'],
  volume: ['LITRE', 'MILLILITRE', 'GALLON'],
  temperature: ['CELSIUS', 'FAHRENHEIT', 'KELVIN']
};

export const OPERATION_CARDS: readonly OperationCard[] = [
  {
    id: 'convert',
    title: 'Convert',
    description: 'Shift across units without losing category context.',
    accent: 'var(--accent-sky)'
  },
  {
    id: 'compare',
    title: 'Compare',
    description: 'Check whether two values represent the same real quantity.',
    accent: 'var(--accent-lime)'
  },
  {
    id: 'add',
    title: 'Add',
    description: 'Combine compatible measurements and return the first unit.',
    accent: 'var(--accent-amber)'
  },
  {
    id: 'subtract',
    title: 'Subtract',
    description: 'Subtract one quantity from another with backend precision.',
    accent: 'var(--accent-pink)'
  },
  {
    id: 'divide',
    title: 'Divide',
    description: 'Calculate a ratio for matching measurement categories.',
    accent: 'var(--accent-violet)'
  }
];
