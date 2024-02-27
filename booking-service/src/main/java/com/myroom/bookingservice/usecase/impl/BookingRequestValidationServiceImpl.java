package com.myroom.bookingservice.usecase.impl;

import com.myroom.bookingservice.api.constants.*;
import com.myroom.bookingservice.api.model.BookingRequestRequestModel;
import com.myroom.bookingservice.api.model.UpdateBookingRequestModel;
import com.myroom.bookingservice.api.model.UpdateContactDetailsRequestModel;
import com.myroom.bookingservice.api.model.UpdatePaymentTypeRequestModel;
import com.myroom.bookingservice.data.dto.BookingRequestModelDto;
import com.myroom.bookingservice.data.dto.RoomAvailabilityRequest;
import com.myroom.bookingservice.data.dto.RoomDetailsDto;
import com.myroom.bookingservice.data.mapper.BookingRequestMapper;
import com.myroom.bookingservice.exception.InvalidBookingRequestDataException;
import com.myroom.bookingservice.exception.InvalidPaymentMethodTypeException;
import com.myroom.bookingservice.exception.InvalidPaymentTypeException;
import com.myroom.bookingservice.repository.BookingRequestRepository;
import com.myroom.bookingservice.usecase.BookingRequestValidationService;
import com.myroom.bookingservice.usecase.OrganizationService;
import com.myroom.bookingservice.usecase.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
@Slf4j
public class BookingRequestValidationServiceImpl implements BookingRequestValidationService {
    @Autowired
    RoomService roomService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    BookingRequestMapper bookingRequestMapper;

    @Autowired
    BookingRequestRepository bookingRequestRepository;

    @Override
    public BookingRequestModelDto validateBookingRequestModelAndGetBookingRequestData(BookingRequestRequestModel bookingRequestModel) {
        log.info("Validating bookingRequestModel data");
        AppConstants.GenericValidInvalidEnum validation = validateBookingRequestData(bookingRequestModel);
        if(validation.equals(AppConstants.GenericValidInvalidEnum.VALID)){
            RoomDetailsDto roomData = roomService.getRoom(bookingRequestModel.getRoomId());
            log.info("setting booking request necessary field(paymentType, paymentMethod, organizationId)");
            BookingRequestModelDto bookingRequestModelDto =  BookingRequestModelDto.builder()
                    .organizationId(roomData.getOrganizationId()) // set the room's organization id
                    .roomId(bookingRequestModel.getRoomId())
                    .uid(bookingRequestModel.getUid())
                    .contactDetails(bookingRequestModel.getContactDetails())
                    .guests(bookingRequestModel.getGuests())
                    .checkIn(bookingRequestModel.getCheckIn())
                    .checkOut(bookingRequestModel.getCheckOut())
                    .build();
            log.info("Created  bookingRequestData: {}", bookingRequestModelDto);
            return bookingRequestModelDto;
        } else{
            log.error("Invalid Booking Request data: {}", bookingRequestModel);
            throw new InvalidBookingRequestDataException(ApiConstants.INVALID_BOOKING_REQUEST_DATA, "Invalid booking request data", "");
        }
    }

    @Override
    public AppConstants.GenericValidInvalidEnum validateUpdateContactDetails(UpdateContactDetailsRequestModel updateContactDetailsRequestModel) {
        //TODO user data validation
        return AppConstants.GenericValidInvalidEnum.VALID;
    }

    @Override
    public AppConstants.GenericValidInvalidEnum validateUpdatePaymentType(UpdatePaymentTypeRequestModel updatePaymentTypeRequestModel) {
        //TODO payment type validation
        return AppConstants.GenericValidInvalidEnum.VALID;
    }

    @Override
    public AppConstants.GenericValidInvalidEnum validateUpdateBookingRequest(UpdateBookingRequestModel updateBookingRequestModel) {
        log.info("Validating update booking request data: {}", updateBookingRequestModel);
        RoomAvailabilityRequest roomAvailabilityRequest = bookingRequestMapper.toRoomAvailabilityRequest(updateBookingRequestModel);
        boolean isRoomAvailable = validateRoomAvailability(roomAvailabilityRequest);
        boolean isValidStatus = validateStatusForUpdate(updateBookingRequestModel.getBookingRequestId());
        if(isRoomAvailable  && isValidStatus){
            log.info("validated update booking request data");
            return AppConstants.GenericValidInvalidEnum.VALID;
        }else{
            log.info("invalid data");
            return AppConstants.GenericValidInvalidEnum.INVALID;
        }
    }

    @Override
    public AppConstants.GenericValidInvalidEnum validateBookingRequestData(BookingRequestRequestModel bookingRequestModel) {
        log.info("Validating booking request data: {}", bookingRequestModel);
        RoomAvailabilityRequest roomAvailabilityRequest = bookingRequestMapper.toRoomAvailabilityRequest(bookingRequestModel);
        // some more validation needed here
        boolean isRoomAvailable = validateRoomAvailability(roomAvailabilityRequest);
        if(isRoomAvailable){
            log.info("booking request data is validated");
            return AppConstants.GenericValidInvalidEnum.VALID;
        }else{
            log.info("booking request data is invalid data");
            return AppConstants.GenericValidInvalidEnum.INVALID;
        }
    }

    private boolean validateStatusForUpdate(String bookingRequestId) {
        log.info("Validating booking request status");
        BookingRequestStatus status = bookingRequestRepository.getStatusById(bookingRequestId);
        if(status.equals(BookingRequestStatus.CANCELLED)){
            log.info("The booking request cannot be updated as its status is set to CANCELED.");
            throw new InvalidBookingRequestDataException(ApiConstants.INVALID_BOOKING_REQUEST_DATA, "Unable to update the booking request as it is canceled.", "");
        }else if(status.equals(BookingRequestStatus.EXPIRED)){
            log.info("The booking request cannot be updated as its status is set to EXPIRED.");
            throw new InvalidBookingRequestDataException(ApiConstants.INVALID_BOOKING_REQUEST_DATA, "Unable to update the booking request as it is expired.", "");
        }else if (status.equals(BookingRequestStatus.CONFIRMED)){
            log.info("The booking request cannot be updated as its status is set to CONFIRMED.");
            throw new InvalidBookingRequestDataException(ApiConstants.INVALID_BOOKING_REQUEST_DATA, "Unable to update the booking request as it is confirmed.", "");
        }
        log.info("Validated booking request status");
        return true;
    }

    private boolean validateRoomAvailability(RoomAvailabilityRequest roomAvailabilityRequest){
        log.info("Validating room availability");
        log.info("Checking room availability for: {}", roomAvailabilityRequest);
        boolean isRoomAvailable = roomService.checkAvailability(roomAvailabilityRequest);
        if(isRoomAvailable){
            log.info("Validated room availability, Room is available");
        }else{
            log.error("Validated room availability, Room unavailable");
        }
        return isRoomAvailable;
    }

    private boolean validateOrganizationId(String organizationId){
        log.info("Validating organizationId");
        boolean isExists = organizationService.isExists(organizationId);
        if(isExists){
            log.info("Validated organizationId, organizationId is correct");
        }else{
            log.info("Validated organizationId, organizationId is incorrect");
        }
        return isExists;
    }

    private boolean validatePaymentType(PaymentType paymentType){
        log.info("Validating PaymentType");
        if(paymentType == null){
            log.info("Validated PaymentType, PaymentType is null");
            return true;
        }
        boolean isValidPaymentType = paymentType.equals(PaymentType.ONLINE_PAYMENT) || paymentType.equals(PaymentType.PAY_AT_HOTEL);
        if(isValidPaymentType){
            log.info("Validated PaymentType, PaymentType is correct");
            return true;
        }else{
            log.info("PaymentType is incorrect");
            throw new InvalidPaymentTypeException(ApiConstants.INVALID_PAYMENT_TYPE, "Invalid payment type "+paymentType, "");
        }
    }

    private boolean validatePaymentMethodType(PaymentMethodType paymentMethodType){
        log.info("Validating paymentMethodType");
        if(paymentMethodType == null){
            log.info("Validated paymentMethodType, paymentMethodType is null");
            return true;
        }
        boolean isValidPaymentMethodType = EnumSet.allOf(PaymentMethodType.class).contains(paymentMethodType);
        if(isValidPaymentMethodType){
            log.info("Validated paymentMethodType, paymentMethodType is correct");
            return true;
        }else{
            log.info("paymentMethodType is incorrect");
            throw new InvalidPaymentMethodTypeException(ApiConstants.INVALID_PAYMENT_METHOD_TYPE, "Invalid payment method type "+paymentMethodType, "");
        }
    }
}