export namespace bookings {
  export interface IGuestList {
    adults: number;
    children: number;
  }

  export interface IContactDetails {
    fullName: string;
    emailId: string;
    phoneNumber: string;
  }

  export enum PaymentType {
    ONLINE_PAYMENT = "ONLINE_PAYMENT",
    PAY_AT_HOTEL = "PAY_AT_HOTEL",
  }

  export enum BookingRequestStatus {
    CONTACT_DETAILS_PENDING = "CONTACT_DETAILS_PENDING",
    PAYMENT_TYPE_PENDING = "PAYMENT_TYPE_PENDING",
    PAYMENT_PENDING = "PAYMENT_PENDING",
    BOOKED = "BOOKED",
    CANCELLED = "CANCELLED",
    CONFIRMED = "CONFIRMED",
    PAY_AT_HOTEL = "PAY_AT_HOTEL",
    EXPIRED = "EXPIRED",
  }

  export enum PaymentMethodType {
    STRIPE,
  }

  export enum BookingStatus {
    PENDING_PAYMENT = "PENDING_PAYMENT",
    PAY_AT_HOTEL = "PAY_AT_HOTEL",
    CONFIRMED = "CONFIRMED",
    CHECKED_IN = "CHECKED_IN",
    CHECKED_OUT = "CHECKED_OUT",
    NO_SHOW = "NO_SHOW",
    CANCELLED = "CANCELLED",
    EXPIRED = "EXPIRED",
  }

  export interface IBookingRequestData {
    id: string;
    roomId: string;
    checkIn: string;
    checkOut: string;
    guests: IGuestList;
    contactDetails: IContactDetails;
    status: BookingRequestStatus;
    paymentType: PaymentType;
    paymentMethodType: PaymentMethodType;
    bookingRequestDate: string;
  }

  export interface ICreateBookingRequest {
    roomId: string;
    checkIn: string;
    checkOut: string;
    guests: IGuestList;
    uid: string;
    contactDetails?: IContactDetails;
  }

  export interface IUpdateContactDetailsRequest {
    bookingRequestId: string;
    details: IContactDetails;
    uid: string;
  }

  export interface IUpdatePaymentTypeRequest {
    bookingRequestId: string;
    uid: string;
    paymentType: PaymentType;
  }

  export interface ICreateBooking {
    bookingRequestId: string;
    uid: string;
  }

  export interface IBookingOrder {
    success: boolean;
    paymentType: PaymentType;
    bookingId: string;
    status: BookingStatus;
    paymentOrderId: string;
    amount: string;
    url: string;
  }

  export interface IRoomPriceMetaData {
    price: string;
    cleaningFee: string;
    roomService: string;
    currency: string;
  }

  export interface IRoomMetaData {
    id: string;
    title: string;
    organizationId: string;
    prices: IRoomPriceMetaData;
  }

  export interface IBookingPaymentMetaDataModel {
    paymentMethodType: PaymentMethodType;
    amount: string;
    currency: string;
    paymentServiceProvider: string;
  }

  export interface IBookingData {
    id: string;
    bookingRequestId: string;
    amount: string;
    checkIn: string;
    checkOut: string;
    roomDetails: IRoomMetaData;
    paymentMetaDataModel: IBookingPaymentMetaDataModel;
    status: BookingStatus;
    guests: IGuestList;
    contactDetails: IContactDetails;
    paymentType: PaymentType;
    bookingDate: string;
    uid: string;
    createdAt: string;
    updatedAt: string;
  }

  export interface IPaymentDetails {
    id: string;
    type: string;
    amount: string;
    currency: string;
    status: string;
    createdAt: string;
  }

  export interface IPageable {
    pageNumber: number;
    pageSize: number;
  }

  export interface IBookings {
    content: IBookingData[];
    pageable: IPageable;
  }
}
