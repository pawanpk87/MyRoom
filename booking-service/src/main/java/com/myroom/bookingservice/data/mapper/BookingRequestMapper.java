package com.myroom.bookingservice.data.mapper;

import com.myroom.bookingservice.api.constants.BookingRequest;
import com.myroom.bookingservice.api.model.BookingRequestRequestModel;
import com.myroom.bookingservice.api.model.BookingRequestResponseModel;
import com.myroom.bookingservice.api.model.UpdateBookingRequestModel;
import com.myroom.bookingservice.data.dto.BookingRequestModelDto;
import com.myroom.bookingservice.data.dto.RoomAvailabilityRequest;
import com.myroom.bookingservice.data.entity.BookingRequestDetails;
import org.springframework.stereotype.Component;

@Component
public class BookingRequestMapper {
    public RoomAvailabilityRequest toRoomAvailabilityRequest(BookingRequestRequestModel bookingRequestModel) {
        Integer guests = bookingRequestModel.getGuests().getAdults() + bookingRequestModel.getGuests().getChildren();
        return RoomAvailabilityRequest.builder()
                .roomId(bookingRequestModel.getRoomId())
                .checkIn(bookingRequestModel.getCheckIn())
                .checkOut(bookingRequestModel.getCheckOut())
                .guests(guests)
                .build();
    }

    public RoomAvailabilityRequest toRoomAvailabilityRequest(UpdateBookingRequestModel updateBookingRequestModel) {
        RoomAvailabilityRequest roomAvailabilityRequest = new RoomAvailabilityRequest();
        roomAvailabilityRequest.setRoomId(updateBookingRequestModel.getRoomId());
        if(updateBookingRequestModel.getGuests() != null){
            Integer guests = updateBookingRequestModel.getGuests().getAdults() + updateBookingRequestModel.getGuests().getChildren();
            roomAvailabilityRequest.setGuests(guests);
        }
        if(updateBookingRequestModel.getCheckIn() != null){
            roomAvailabilityRequest.setCheckIn(updateBookingRequestModel.getCheckIn());
        }
        if(updateBookingRequestModel.getCheckOut() != null){
            roomAvailabilityRequest.setCheckOut(updateBookingRequestModel.getCheckOut());
        }
        return roomAvailabilityRequest;
    }

    public BookingRequestDetails toBookingRequest(BookingRequestRequestModel bookingRequestModel) {
        BookingRequestDetails bookingRequest = new BookingRequestDetails();
        bookingRequest.setRoomId(bookingRequestModel.getRoomId());
        bookingRequest.setCheckIn(bookingRequestModel.getCheckIn());
        bookingRequest.setCheckOut(bookingRequestModel.getCheckOut());
        bookingRequest.setGuests(bookingRequestModel.getGuests());
        bookingRequest.setUid(bookingRequestModel.getUid());
        bookingRequest.setContactDetails(bookingRequestModel.getContactDetails());
        return bookingRequest;
    }

    public BookingRequestResponseModel toBookingResponseModel(BookingRequestDetails bookingRequestDetails) {
        BookingRequestRequestModel bookingRequestModel = BookingRequestRequestModel.builder()
                .checkIn(bookingRequestDetails.getCheckIn())
                .checkOut(bookingRequestDetails.getCheckOut())
                .guests(bookingRequestDetails.getGuests())
                .roomId(bookingRequestDetails.getRoomId())
                .uid(bookingRequestDetails.getUid())
                .contactDetails(bookingRequestDetails.getContactDetails())
                .build();

        BookingRequest bookingRequest = BookingRequest.builder()
                .bookingRequestId(bookingRequestDetails.getId())
                .bookingRequestDate(bookingRequestDetails.getBookingRequestDate().toString())
                .status(bookingRequestDetails.getStatus())
                .paymentType(bookingRequestDetails.getPaymentType())
                .paymentMethodType(bookingRequestDetails.getPaymentMethodType())
                .details(bookingRequestModel)
                .build();

        return BookingRequestResponseModel.builder()
                .id(bookingRequestDetails.getId())
                .checkIn(bookingRequestDetails.getCheckIn())
                .checkOut(bookingRequestDetails.getCheckOut())
                .guests(bookingRequestDetails.getGuests())
                .roomId(bookingRequestDetails.getRoomId())
                .contactDetails(bookingRequestDetails.getContactDetails())
                .status(bookingRequestDetails.getStatus())
                .paymentType(bookingRequestDetails.getPaymentType())
                .paymentMethodType(bookingRequestDetails.getPaymentMethodType())
                .bookingRequestDate(bookingRequestDetails.getBookingRequestDate())
                .build();
    }

    public BookingRequestRequestModel toBookingRequestModel(BookingRequestDetails bookingRequestDetails) {
        return BookingRequestRequestModel.builder()
                .roomId(bookingRequestDetails.getRoomId())
                //.organizationId(bookingRequestDetails.getOrganizationId())
                .checkIn(bookingRequestDetails.getCheckIn())
                .checkOut(bookingRequestDetails.getCheckOut())
                .guests(bookingRequestDetails.getGuests())
                .contactDetails(bookingRequestDetails.getContactDetails())
                //.paymentType(bookingRequestDetails.getPaymentType())
                //.paymentMethodType(bookingRequestDetails.getPaymentMethodType())
                .uid(bookingRequestDetails.getUid())
                .build();
    }

    public BookingRequestDetails toBookingRequestDetails(BookingRequestModelDto bookingRequestModelDto) {
        BookingRequestDetails bookingRequest = new BookingRequestDetails();
        bookingRequest.setRoomId(bookingRequestModelDto.getRoomId());
        bookingRequest.setCheckIn(bookingRequestModelDto.getCheckIn());
        bookingRequest.setCheckOut(bookingRequestModelDto.getCheckOut());
        bookingRequest.setGuests(bookingRequestModelDto.getGuests());
        bookingRequest.setOrganizationId(bookingRequestModelDto.getOrganizationId());
        bookingRequest.setUid(bookingRequestModelDto.getUid());
        bookingRequest.setContactDetails(bookingRequestModelDto.getContactDetails());
        bookingRequest.setPaymentType(bookingRequestModelDto.getPaymentType());
        bookingRequest.setPaymentMethodType(bookingRequestModelDto.getPaymentMethodType());
        return bookingRequest;
    }
}
