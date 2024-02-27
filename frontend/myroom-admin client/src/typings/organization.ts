export namespace organizations {
  export interface IAddresses {
    street1: string;
    street2?: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
  }

  export enum IAdminType {
    SUPER_ADMIN,
    ADMIN,
  }

  export interface IAdmin {
    uid: string;
    adminType: IAdminType;
  }

  export interface IBusinessProfile {
    businessType: string;
    category: string;
    subcategory: string;
    addresses: IAddresses;
  }

  export interface IOrganization {
    id: string;
    name: string;
    email: string;
    description: string;
    phone: string;
    superAdmin: IAdmin | null;
    admins?: IAdmin[];
    businessProfile: IBusinessProfile;
  }
}
