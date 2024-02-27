export interface IUser {
  uid: string;
  name: string;
  email: string;
  isEmailVerified: boolean;
  providerId: string;
  picture: string;
}

export interface IRoomPriceMetaDataModel {
  price: string;
  cleaningFee: string;
  roomService: string;
  currency: string;
}

export interface IRoomMetaDataModel {
  id: string;
  title: string;
  organizationId: string;
  prices: IRoomPriceMetaDataModel;
}

export interface IContactDetails {
  fullName: string;
  emailId: string;
  phoneNumber: string;
}

export interface IBookingData {
  id: string;
  amount: string;
  checkIn: string;
  checkOut: string;
  roomDetails: IRoomMetaDataModel;
  status: string;
  contactDetails: IContactDetails;
  uid: string;
}
