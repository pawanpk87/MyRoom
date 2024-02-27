package com.myroom.bookingservice.usecase.impl;

import com.myroom.bookingservice.api.constants.*;
import com.myroom.bookingservice.api.model.*;
import com.myroom.bookingservice.data.dto.BookingRequestModelDto;
import com.myroom.bookingservice.data.entity.BookingRequestDetails;
import com.myroom.bookingservice.data.mapper.BookingRequestMapper;
import com.myroom.bookingservice.event.BookingRequestEvent;
import com.myroom.bookingservice.exception.*;
import com.myroom.bookingservice.repository.BookingRequestRepository;
import com.myroom.bookingservice.usecase.BookingRequestService;
import com.myroom.bookingservice.usecase.BookingRequestValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class BookingRequestServiceImpl implements BookingRequestService {
    @Autowired
    BookingRequestRepository bookingRequestRepository;

    @Autowired
    BookingRequestMapper bookingRequestMapper;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    BookingRequestValidationService bookingRequestValidationService;

    @Override
    public BookingRequestResponseModel createBookingRequest(BookingRequestRequestModel bookingRequestModel) {
        log.info("Creating booking request");
        BookingRequestModelDto bookingRequestModelDto = bookingRequestValidationService.validateBookingRequestModelAndGetBookingRequestData(bookingRequestModel);
        BookingRequestDetails bookingRequest = bookingRequestMapper.toBookingRequestDetails(bookingRequestModelDto);
        bookingRequest.setStatus(BookingRequestStatus.CONTACT_DETAILS_PENDING); // make status contact details pending
        bookingRequest.setBookingRequestDate(Instant.now());
        try {
            log.info("Saving booking request details: {}", bookingRequest);
            BookingRequestDetails savedBookingRequest = bookingRequestRepository.save(bookingRequest);
            log.info("Saved bookingRequest details: {}", savedBookingRequest);
            BookingRequestResponseModel bookingResponseModel = bookingRequestMapper.toBookingResponseModel(savedBookingRequest);
            eventPublisher.publishEvent(new BookingRequestEvent(this, BookingRequestEvent.EventType.CREATED, savedBookingRequest));
            return bookingResponseModel;
        }catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new BookingServiceRuntimeException(ex.getMessage());
        }
    }

    @Override
    public BookingRequestResponseModel updateContactDetails(String bookingRequestId, UpdateContactDetailsRequestModel updateContactDetailsRequestModel) {
        updateContactDetailsRequestModel.setBookingRequestId(bookingRequestId);

        log.info("Updating booking request contact details");

        BookingRequestDetails prevBookingRequestDetails = getBookingRequestData(bookingRequestId);
        log.info("Fetched previous booking request data: {}", prevBookingRequestDetails);

        // set the roomId
        AppConstants.GenericValidInvalidEnum validation = bookingRequestValidationService.validateUpdateContactDetails(updateContactDetailsRequestModel);

        if(validation.equals(AppConstants.GenericValidInvalidEnum.VALID)){
            validateCurrentUser(updateContactDetailsRequestModel.getUid(), prevBookingRequestDetails.getUid());
            log.info("Getting updated booking request data from previous booking request data");
            BookingRequestDetails updatedBookingRequestDetails = getUpdatedBookingRequestDetails(prevBookingRequestDetails, updateContactDetailsRequestModel);
            log.info("New updated booking request data: {}", updatedBookingRequestDetails);
            try {
                prevBookingRequestDetails.setStatus(BookingRequestStatus.PAYMENT_TYPE_PENDING); // update status to payment type pending
                log.info("Updating booking request contact details with new data");
                BookingRequestDetails savedBookingRequest = bookingRequestRepository.save(updatedBookingRequestDetails);
                log.info("Updated booking request: {}", savedBookingRequest);
                BookingRequestResponseModel bookingResponseModel = bookingRequestMapper.toBookingResponseModel(savedBookingRequest);
                eventPublisher.publishEvent(new BookingRequestEvent(this, BookingRequestEvent.EventType.UPDATED, savedBookingRequest));
                return bookingResponseModel;
            }catch (Exception ex){
                log.error("Some error curred: {}", ex.getMessage());
                throw new BookingServiceRuntimeException(ex.getMessage());
            }
        }else{
            log.error("Invalid Booking Request data: {}", updateContactDetailsRequestModel);
            throw new InvalidBookingRequestDataException(ApiConstants.MESSAGE_INVALID_BOOKING_REQUEST_DATA, "Invalid Booking request data", "");
        }
    }

    @Override
    public BookingRequestResponseModel updatePaymentType(String bookingRequestId, UpdatePaymentTypeRequestModel updatePaymentTypeRequestModel) {
        updatePaymentTypeRequestModel.setBookingRequestId(bookingRequestId);

        log.info("Updating booking request payment type");

        BookingRequestDetails prevBookingRequestDetails = getBookingRequestData(bookingRequestId);
        log.info("Fetched previous booking request data: {}", prevBookingRequestDetails);

        // set the roomId
        AppConstants.GenericValidInvalidEnum validation = bookingRequestValidationService.validateUpdatePaymentType(updatePaymentTypeRequestModel);

        if(validation.equals(AppConstants.GenericValidInvalidEnum.VALID)){
            validateCurrentUser(updatePaymentTypeRequestModel.getUid(), prevBookingRequestDetails.getUid());
            log.info("Getting updated booking request data from previous booking request data");
            BookingRequestDetails updatedBookingRequestDetails = getUpdatedBookingRequestDetails(prevBookingRequestDetails, updatePaymentTypeRequestModel);
            if(updatedBookingRequestDetails.getPaymentType().equals(PaymentType.ONLINE_PAYMENT)){
                updatedBookingRequestDetails.setPaymentMethodType(PaymentMethodType.STRIPE);
            }
            log.info("New updated booking request data: {}", updatedBookingRequestDetails);
            try {
                updatedBookingRequestDetails.setStatus(BookingRequestStatus.PAYMENT_PENDING); // update status to payment pending
                log.info("Updating booking request details with new data");
                BookingRequestDetails savedBookingRequest = bookingRequestRepository.save(updatedBookingRequestDetails);
                log.info("Updated booking request: {}", savedBookingRequest);
                BookingRequestResponseModel bookingResponseModel = bookingRequestMapper.toBookingResponseModel(savedBookingRequest);
                eventPublisher.publishEvent(new BookingRequestEvent(this, BookingRequestEvent.EventType.UPDATED, savedBookingRequest));
                return bookingResponseModel;
            }catch (Exception ex){
                log.error("Some error curred: {}", ex.getMessage());
                throw new BookingServiceRuntimeException(ex.getMessage());
            }
        }else{
            log.error("Invalid Booking Request data: {}", updatePaymentTypeRequestModel);
            throw new InvalidBookingRequestDataException(ApiConstants.MESSAGE_INVALID_BOOKING_REQUEST_DATA, "Invalid Booking request data", "");
        }
    }

    @Override
    public void updateStatus(String bookingRequestId, BookingRequestStatus status) {
        log.info("Updating booking request status for booking bookingRequestId:{}", bookingRequestId);
        try {
            bookingRequestRepository.updateStatusById(status, bookingRequestId);
        }catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new BookingServiceRuntimeException(ex.getMessage());
        }
    }

    @Override
    public BookingRequestResponseModel updateBookingRequest(String bookingRequestId, UpdateBookingRequestModel updateBookingRequestModel) {
        updateBookingRequestModel.setBookingRequestId(bookingRequestId);

        log.info("Updating booking request");

        BookingRequestDetails prevBookingRequestDetails = getBookingRequestData(bookingRequestId);
        log.info("Fetched previous booking request data: {}", prevBookingRequestDetails);

        // set the roomId
        updateBookingRequestModel.setRoomId(prevBookingRequestDetails.getRoomId());

        AppConstants.GenericValidInvalidEnum validation = bookingRequestValidationService.validateUpdateBookingRequest(updateBookingRequestModel);

        if(validation.equals(AppConstants.GenericValidInvalidEnum.VALID)){
            validateCurrentUser(updateBookingRequestModel.getUid(), prevBookingRequestDetails.getUid());
            log.info("Getting updated booking request data from previous booking request data");
            BookingRequestDetails updatedBookingRequestDetails = getUpdatedBookingRequestDetails(prevBookingRequestDetails, updateBookingRequestModel);
            log.info("New updated booking request data: {}", updatedBookingRequestDetails);
            try {
                log.info("Updating booking request with new data");
                BookingRequestDetails savedBookingRequest = bookingRequestRepository.save(updatedBookingRequestDetails);
                log.info("Updated booking request: {}", savedBookingRequest);
                BookingRequestResponseModel bookingResponseModel = bookingRequestMapper.toBookingResponseModel(savedBookingRequest);
                eventPublisher.publishEvent(new BookingRequestEvent(this, BookingRequestEvent.EventType.UPDATED, savedBookingRequest));
                return bookingResponseModel;
            }catch (Exception ex){
                log.error("Some error curred: {}", ex.getMessage());
                throw new BookingServiceRuntimeException(ex.getMessage());
            }
        }else{
            log.error("Invalid Booking Request data: {}", updateBookingRequestModel);
            throw new InvalidBookingRequestDataException(ApiConstants.MESSAGE_INVALID_BOOKING_REQUEST_DATA, "Invalid Booking request data", "");
        }
    }

    @Override
    public BookingRequestResponseModel getBookingRequest(String bookingRequestId) {
        log.info("Finding bookingRequest");
        Optional<BookingRequestDetails> bookingRequest = bookingRequestRepository.findById(bookingRequestId);
        if(bookingRequest.isEmpty()){
            log.info("could not find any bookingRequest with id: {}", bookingRequestId);
            throw new BookingRequestNotFoundException(ApiConstants.INVALID_BOOKING_REQUEST_ID, "Booking request not found for the provided ID!", "");
        }
        BookingRequestResponseModel bookingResponseModel = bookingRequestMapper.toBookingResponseModel(bookingRequest.get());
        return bookingResponseModel;
    }

    @Override
    public CancelBookingRequestResponseModel cancelBookingRequest(String bookingRequestId, CancelBookingRequestModel cancelBookingRequestModel) {
        BookingRequestDetails bookingRequestDetails = getBookingRequestData(bookingRequestId);
        validateCurrentUser(cancelBookingRequestModel.getUid(), bookingRequestDetails.getUid());
        log.info("Checking if the booking request is canceled, confirmed, or expired");
        if(isBookingRequestCanceled(bookingRequestDetails)){
            return getCancelBookingRequestResponseModel(false, "Booking request is already canceled.");
        }
        if(isBookingRequestConfirmed(bookingRequestDetails)){
            return getCancelBookingRequestResponseModel(false, "Booking request is confirmed.");
        }
        if(isBookingRequestExpired(bookingRequestDetails)){
            return getCancelBookingRequestResponseModel(false, "Booking request is expired.");
        }
        try {
            log.info("Updating status to CANCELLED");
            bookingRequestDetails.setStatus(BookingRequestStatus.CANCELLED);
            BookingRequestDetails updatedBookingRequestDetails = bookingRequestRepository.save(bookingRequestDetails);
            log.info("updated status: {}", updatedBookingRequestDetails);
            eventPublisher.publishEvent(new BookingRequestEvent(this, BookingRequestEvent.EventType.CANCELLED, updatedBookingRequestDetails));
            CancelBookingRequestResponseModel cancelBookingRequestResponseModel =
                    new CancelBookingRequestResponseModel(true, "Booking Canceled");
            return cancelBookingRequestResponseModel;
        }catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new BookingServiceRuntimeException(ex.getMessage());
        }
    }

    private CancelBookingRequestResponseModel getCancelBookingRequestResponseModel(boolean success, String message){
        log.info(message);
        CancelBookingRequestResponseModel cancelBookingRequestResponseModel =
                new CancelBookingRequestResponseModel(success, message);
        return cancelBookingRequestResponseModel;
    }

    @Override
    public BookingRequestDetails getBookingRequestData(String bookingRequestId){
        log.info("Fetching booking request data for bookingRequestId: {}", bookingRequestId);
        Optional<BookingRequestDetails> bookingRequestDetailsOpt = bookingRequestRepository.findById(bookingRequestId);
        if(bookingRequestDetailsOpt.isEmpty()){
            log.info("could not find any bookingRequest with id: {}", bookingRequestId);
            throw new BookingRequestNotFoundException(ApiConstants.INVALID_BOOKING_REQUEST_ID, "Booking request not found for the provided ID!", "");
        }
        BookingRequestDetails bookingRequestDetails = bookingRequestDetailsOpt.get();
        log.info("Fetched booking request data: {}", bookingRequestDetails);
        return bookingRequestDetails;
    }

    private void validateCurrentUser(String currentUserUid, String prevUserUid){
        log.info("Validating current user is the one who created the booking request");
        if(currentUserUid.equals(prevUserUid) == false) {
            log.error("Unauthorized access: Current user '{}' is not the creator of the booking request", currentUserUid);
            throw new BookingRequestUnauthorizedAccessException(ApiConstants.UNAUTHORIZED, "You do not have permission to modify this booking request.", "");
        }
        log.info("Validated current user");
    }

    private boolean isBookingRequestCanceled(BookingRequestDetails bookingRequestDetails){
        return bookingRequestDetails.getStatus().equals(BookingRequestStatus.CANCELLED);
    }

    private boolean isBookingRequestConfirmed(BookingRequestDetails bookingRequestDetails){
        return bookingRequestDetails.getStatus().equals(BookingRequestStatus.CONFIRMED);
    }

    private boolean isBookingRequestExpired(BookingRequestDetails bookingRequestDetails){
        return bookingRequestDetails.getStatus().equals(BookingRequestStatus.EXPIRED);
    }

    private BookingRequestDetails getUpdatedBookingRequestDetails(BookingRequestDetails prevBookingRequestDetails, UpdateBookingRequestModel updateBookingRequestModel) {
        if(updateBookingRequestModel.getCheckIn() != null){
            prevBookingRequestDetails.setCheckIn(updateBookingRequestModel.getCheckIn());
        }
        if(updateBookingRequestModel.getCheckOut() != null){
            prevBookingRequestDetails.setCheckOut(updateBookingRequestModel.getCheckOut());
        }
        if(updateBookingRequestModel.getGuests() != null){
            prevBookingRequestDetails.setGuests(updateBookingRequestModel.getGuests());
        }
        return prevBookingRequestDetails;
    }

    private BookingRequestDetails getUpdatedBookingRequestDetails(BookingRequestDetails prevBookingRequestDetails, UpdateContactDetailsRequestModel updateContactDetailsRequestModel) {
        prevBookingRequestDetails.setContactDetails(updateContactDetailsRequestModel.getDetails());
        return prevBookingRequestDetails;
    }

    private BookingRequestDetails getUpdatedBookingRequestDetails(BookingRequestDetails prevBookingRequestDetails, UpdatePaymentTypeRequestModel updatePaymentTypeRequestModel) {
        prevBookingRequestDetails.setPaymentType(updatePaymentTypeRequestModel.getPaymentType());
        return prevBookingRequestDetails;
    }
}