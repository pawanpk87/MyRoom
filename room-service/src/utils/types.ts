export enum GenericValidInvalidEnum {
  VALID,
  INVALID,
}

export interface IRoomServiceError {
  name: string;
  errorCode: string;
  message: string;
  details: string;
  status: number;
}
