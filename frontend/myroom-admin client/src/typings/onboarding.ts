export namespace onboarding {
  export interface IAddresses {
    street1: string;
    street2?: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
  }

  export interface IBusinessProfile {
    businessType: string;
    category: string;
    subcategory: string;
    addresses: IAddresses;
  }

  export interface IOnboardOrganization {
    name: string;
    email: string;
    phone: string;
    description: string;
    businessProfile: IBusinessProfile;
    uid: string;
  }

  export interface IOrganizationAccountOnboarding {
    id: string;
    uid: string;
  }
}
