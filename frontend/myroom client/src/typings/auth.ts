export namespace auth {
  export enum AuthStatus {
    SUCCESS = "SUCCESS",
    FAILURE = "FAILURE",
    EXPIRED = "EXPIRED",
    INVALID = "INVALID",
  }

  export interface IVerifyTokenResponse {
    status: AuthStatus;
    message: string;
  }

  export interface IUser {
    uid: string;
    name: string;
    email: string;
    isEmailVerified: boolean;
    providerId: string;
    picture: string;
  }
}
