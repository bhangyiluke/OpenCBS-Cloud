export interface Currency {
  id: number;
  name: string;
  code: string;
  main: boolean;
}

export interface ShareProduct {
  id?: number;
  name: string;
  code: string;
  currency?: Currency;
  currencyId?: number;
  nominalValue?: number;
  unitPrice?: number;
  minSharesPerMember?: number;
  maxSharesPerMember?: number;
  allowMemberTransfers?: boolean;
  lotSelectionPolicy?: 'FIFO' | 'LIFO';
  statusType?: 'ACTIVE' | 'INACTIVE';
}

export interface ShareLot {
  id?: number;
  shareProduct?: ShareProduct;
  profileId?: number;
  profileName?: string;
  quantity?: number;
  availableQuantity?: number;
  unitPrice?: number;
  totalAmount?: number;
  acquisitionDate?: string;
  ageInDays?: number;
  sourceTransactionId?: number;
  sourceTransactionType?: string;
  status?: string;
}

export interface ShareTransaction {
  id?: number;
  shareProduct?: ShareProduct;
  sourceProfileId?: number;
  sourceProfileName?: string;
  destinationProfileId?: number;
  destinationProfileName?: string;
  type?: string;
  status?: string;
  quantity?: number;
  unitPrice?: number;
  totalAmount?: number;
  transactionDate?: string;
  reason?: string;
  idempotencyKey?: string;
}

export interface ShareAgeBucket {
  label: string;
  quantity: number;
  value: number;
}

export interface ShareProductSummary {
  shareProductId: number;
  shareProductCode: string;
  shareProductName: string;
  quantity: number;
  value: number;
}

export interface MemberStatusSummary {
  status: string;
  quantity: number;
  value: number;
}

export interface SaccoSharePortfolio {
  totalIssuedShares: number;
  totalShareValue: number;
  byProduct: ShareProductSummary[];
  byAgeBucket: ShareAgeBucket[];
  byMemberStatus: MemberStatusSummary[];
}

export interface ShareAgeAnalysis {
  totalQuantity: number;
  totalValue: number;
  buckets: ShareAgeBucket[];
}

export interface MemberSharePortfolio {
  profileId: number;
  profileName: string;
  totalQuantity: number;
  totalValue: number;
  lots: ShareLot[];
  transactions: ShareTransaction[];
}
