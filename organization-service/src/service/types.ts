export interface IGetOrganizations {
  organizaions: any;
  totalRecods: number;
  totalPages: number;
  currentPage: number;
}

export interface IVerifyUser {
  allowed: boolean;
  message: string;
}
