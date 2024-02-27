export interface Addresses {
  street1: string;
  street2?: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
}

export enum AdminType {
  SUPER_ADMIN = "SUPER_ADMIN",
  ADMIN = "ADMIN",
}

export interface Admin {
  uid: string;
  adminType: AdminType;
}

export interface BusinessProfile {
  businessType: string;
  category: string;
  subcategory: string;
  addresses: Addresses;
}

export interface Organization {
  name: string;
  email: string;
  description: string;
  phone: string;
  superAdmin: Admin | null;
  admins?: Admin[];
  businessProfile: BusinessProfile;
}
