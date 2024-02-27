import { payments, rooms } from ".";

export namespace bookings {
  export interface IContactDetails {
    emailId: string;
    fullName: string;
    phoneNumber: string;
  }

  export interface IGuests {
    adults: number;
    children: number;
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

  export interface IBookingRoomDetails {
    id: string;
    title: string;
    prices: rooms.Prices;
    organizationId: string;
  }

  export interface IPaymentMetaDataModel {
    amount: string;
    currency: string;
    paymentMethodType: payments.IPaymentMethodType;
    stripePaymentServiceProvider: payments.IStripePaymentServiceProvider;
  }

  export interface IPageable {
    pageNumber: number;
    pageSize: number;
  }

  export enum QueryRelationalOperator {
    EQUAL,
    NOT_EQUAL,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN_OR_EQUAL,
    IN,
  }

  export enum SortingType {
    ASC,
    DESC,
  }

  export interface IBookingDateRequestModel {
    bookingDate?: string;
    operator?: QueryRelationalOperator;
    sortingType?: SortingType;
  }

  export interface ICheckInDateRequestModel {
    checkIn?: string;
    operator?: QueryRelationalOperator;
    sortingType?: SortingType;
  }

  export interface ICheckOutDateRequestModel {
    checkOut?: string;
    operator?: QueryRelationalOperator;
    sortingType?: SortingType;
  }

  export interface IAmountRequestModel {
    amount: string;
    operator: QueryRelationalOperator;
    sortingType: SortingType;
  }

  export interface IGetBookingsQuery {
    id?: string;
    bookingDate?: IBookingDateRequestModel;
    checkInDate?: ICheckInDateRequestModel;
    checkOutDate?: ICheckOutDateRequestModel;
    amount?: IAmountRequestModel;
    status?: BookingStatus;
    page?: number;
    size?: number;
  }

  export interface IBooking {
    id: string;
    amount: string;
    bookingDate: string;
    bookingRequestId: string;
    checkIn: string;
    checkOut: string;
    contactDetails: IContactDetails;
    createdAt: string;
    guests: string;
    paymentType: string;
    status: string;
    roomDetails: IBookingRoomDetails;
    uid: string;
    updatedAt: string;
    paymentMetaDataModel: string;
  }

  export interface IBookings {
    content: IBooking[];
    pageable: IPageable;
  }

  export interface IBookingUpdateData {
    bookingId: string;
    status: string;
  }

  export interface IBookingsStatistic {
    totalBookings: number;
    total: number;
    data: Number[];
  }

  export interface IGetBookingCount {
    checkIn: number;
    checkOut: number;
  }

  export interface IGetBookingStatisticQuery {
    organizationId: string;
    startDate: string;
    endDate: string;
  }

  export interface IGetPeopleCountsQuery {
    organizationId: string;
  }

  export interface IGetPeopleCountsQueryData {
    singleCounts: number;
    doubleCounts: number;
    othersCounts: number;
  }

  export interface IGetBookingsRecordsQuery {
    organizationId: string;
    startDate: string;
    endDate: string;
  }

  export interface IGetBookingsRecordsQueryData {
    total: number;
    data: IBooking[];
  }
}
