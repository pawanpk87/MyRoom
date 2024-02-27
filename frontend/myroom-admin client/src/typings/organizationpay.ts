export namespace organizationpay {
  export enum OrganizationAccountStatus {
    ACTIVE = "ACTIVE",
    INACTIVE = "INACTIVE",
  }

  export enum StripeAccountStatus {
    ACCOUNT_ONBOARDING_PENDING = "ACCOUNT_ONBOARDING_PENDING",
    ACTIVE = "ACTIVE",
    INACTIVE = "INACTIVE",
  }

  export interface StripeAccount {
    accountId: string;
    status: StripeAccountStatus;
  }

  export interface IOrganizationAccount {
    id: string;
    organizationId: string;
    status: OrganizationAccountStatus;
    stripeAccountDetails: StripeAccount;
    createdAt: string;
    updatedAt: string;
  }
}
