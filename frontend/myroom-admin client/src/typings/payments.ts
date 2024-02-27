export namespace payments {
  export enum IPaymentMethodType {
    STRIPE,
  }

  export enum StripePaymentStatus {
    open,
    complete,
    expired,
  }

  export interface IStripePaymentServiceProvider {
    id: string;
    amount: string;
    status: StripePaymentStatus;
    url: string;
  }

  export interface IRevenuStatistic {
    totalRevenue: number;
    total: number;
    data: Number[];
  }

  export interface IGetPaymentStatisticQuery {
    organizationId: string;
    startDate: string;
    endDate: string;
  }

  export interface IGetRevenueStatisticQuery {
    organizationId: string;
    duration: string;
  }

  export interface IGetRevenueStatisticData {
    curr: number[];
    prev: number[];
  }
}
