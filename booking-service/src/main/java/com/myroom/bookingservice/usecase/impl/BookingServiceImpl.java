package com.myroom.bookingservice.usecase.impl;

import com.myroom.bookingservice.api.constants.*;
import com.myroom.bookingservice.api.model.*;
import com.myroom.bookingservice.api.model.query.GetAllBookingsQuery;
import com.myroom.bookingservice.data.dto.*;
import com.myroom.bookingservice.data.entity.BookingDetails;
import com.myroom.bookingservice.data.entity.BookingRequestDetails;
import com.myroom.bookingservice.data.mapper.BookingMapper;
import com.myroom.bookingservice.data.mapper.BookingRequestMapper;
import com.myroom.bookingservice.data.mapper.RoomMapper;
import com.myroom.bookingservice.data.model.BookingPaymentMetaDataModel;
import com.myroom.bookingservice.data.model.StripePaymentServiceProvider;
import com.myroom.bookingservice.data.model.RoomMetaDataModel;
import com.myroom.bookingservice.exception.*;
import com.myroom.bookingservice.repository.BookingRepository;
import com.myroom.bookingservice.usecase.*;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingRequestService bookingRequestService;

    @Autowired
    RoomService roomService;

    @Autowired
    BookingRequestValidationService bookingRequestValidationService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    BookingRequestMapper bookingRequestMapper;

    @Autowired
    BookingMapper bookingMapper;

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    BookingRepository bookingRepository;


    @Override
    public BookingOrderResponseModel createBookingOrder(String bookingRequestId, BookingOrderRequestModel bookingOrderRequestModel) {
        bookingOrderRequestModel.setBookingRequestId(bookingRequestId);

        BookingRequestDetails bookingRequestDetails = bookingRequestService.getBookingRequestData(bookingRequestId);

        BookingRequestRequestModel bookingRequestModel = bookingRequestMapper.toBookingRequestModel(bookingRequestDetails);
        AppConstants.GenericValidInvalidEnum validation = bookingRequestValidationService.validateBookingRequestData(bookingRequestModel);

        if(validation.equals(AppConstants.GenericValidInvalidEnum.VALID)){
            BookingOrderResponseModel bookingOrderResponseModel = null;

            validateCurrentUser(bookingOrderRequestModel.getUid(), bookingRequestDetails.getUid());

            BookingDetails bookingDetails = createBookingRecord(bookingOrderRequestModel, bookingRequestDetails);

            PaymentType type = bookingRequestDetails.getPaymentType();

            switch (type){
                case PAY_AT_HOTEL:{
                    updateStatus(bookingDetails, BookingStatus.PAY_AT_HOTEL);
                    log.info("Redirecting to {} as the payment type is: {}", type, type);
                    bookingOrderResponseModel = bookingMapper.toBookingOrderResponseModel(bookingDetails);
                    bookingRequestService.updateStatus(bookingDetails.getBookingRequestId(), BookingRequestStatus.BOOKED);
                    break;
                }

                case ONLINE_PAYMENT:{
                    log.info("Redirecting to {} for payment as the payment type is: {}", type, type);
                    OnlinePaymentOrderResponseDto onlinePaymentOrderResponseDto = doOnlinePayment(bookingDetails);
                    bookingOrderResponseModel = bookingMapper.toBookingOrderResponseModel(onlinePaymentOrderResponseDto, bookingDetails);
                    break;
                }

                default:{
                    log.info("Booking cannot be processed due to an invalid payment type: {}", type);
                    throw new InvalidPaymentTypeException(ApiConstants.INVALID_PAYMENT_TYPE, "Invalid payment type", "");
                }
           }
            log.info("Booking Order: {}", bookingOrderResponseModel);
            return bookingOrderResponseModel;
        }else{
            log.error("Invalid Booking Request: {}", bookingRequestDetails);
            throw new InvalidBookingRequestDataException(ApiConstants.INVALID_BOOKING_REQUEST_DATA, "Booking cannot be processed due to an invalid booking request data", "");
        }
    }

    @Override
    public BookingDataResponseModel getBookingDataByBookingRequestId(String bookingRequestId) {
        log.info("Fetching the bookingId for booking request id:{}", bookingRequestId);
        Optional<String> bookingId = bookingRepository.findBookingIdByBookingRequestId(bookingRequestId);
        if(bookingId.isEmpty()){
            log.error("Could not find any booking for for bookingRequestId:{}", bookingRequestId);
            throw new BookingRequestNotFoundException(ApiConstants.NOT_FOUND, "Could not find any booking for bookingRequestId:"+bookingRequestId, null);
        }
        log.info("bookingId for bookingRequestId:{} is :{}", bookingRequestId, bookingId.get());
        return getBookingDataById(bookingId.get());
    }

    @Override
    public BookingDataResponseModel getBookingDataById(String id) {
        return bookingMapper.toBookingDataResponseModel(getBookingDataB(id));
    }

    private BookingDetails getBookingDataB(String id){
        log.info("Fetching booking data for bookingId:{}", id);
        Optional<BookingDetails> bookingDetails = bookingRepository.findById(id);
        if(bookingDetails.isEmpty()){
            log.error("Could not find any booking for bookingId:{}", id);
            throw new BookingNotFoundException(ApiConstants.NOT_FOUND, "Could not find any booking for bookingId:"+id, null);
        }
        log.info("Fetched booking data:{}", bookingDetails.get());
        return bookingDetails.get();
    }

    @Override
    public void handleBookingPaymentCancel(String bookingId) {
        log.info("handling booking payment cancel for bookingId:{}", bookingId);
        // TODO: Delete the booking request and booking record
    }

    @Override
    public void updateBookingStatusAndBookingPaymentMetaDataModel(String bookingId, String status) {
        log.info("Updating booking status and paymentMetaData for bookingId:{}", bookingId);
        BookingDetails bookingDetails = getBookingDataB(bookingId);
        if("complete".equals(status)){
            bookingDetails.setStatus(BookingStatus.CONFIRMED);
            StripePaymentServiceProvider stripePaymentServiceProvider = (StripePaymentServiceProvider) bookingDetails.getPaymentMetaDataModel().getStripePaymentServiceProvider();
            stripePaymentServiceProvider.setStatus(StripePaymentServiceProvider.StripePaymentStatus.complete);

            BookingDetails savedBookingDetails = bookingRepository.save(bookingDetails);
            log.info("Updated booking status and paymentMetaData: {}", savedBookingDetails);
        }
    }

    @Override
    public Page<BookingDetails> getBookings(GetAllBookingsQuery criteria) {
        int page = criteria.getPage() == null ? 0 : criteria.getPage();
        int size = criteria.getSize() == null ? 10 : criteria.getSize();

        Pageable pr = PageRequest.of(page, size);

        Specification<BookingDetails> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getId() != null) {
                predicates.add(cb.like(root.get("id"), "%" + criteria.getId() + "%"));
            }

            if(criteria.getUid() != null){
                predicates.add(cb.like(root.get("uid"), criteria.getUid()));
            }

            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
            }

            if (criteria.getAmount() != null && criteria.getAmount().getSortingType().equals(SortingType.ASC)) {
                query.orderBy(cb.asc(root.get("amount")));
            } else if (criteria.getAmount() != null && criteria.getAmount().getSortingType().equals(SortingType.DESC)) {
                query.orderBy(cb.desc(root.get("amount")));
            }

            if (criteria.getBookingDate() != null && criteria.getBookingDate().getSortingType().equals(SortingType.ASC)) {
                query.orderBy(cb.asc(root.get("bookingDate")));
            } else if (criteria.getBookingDate() != null && criteria.getBookingDate().getSortingType().equals(SortingType.DESC)) {
                query.orderBy(cb.desc(root.get("bookingDate")));
            }

            if (criteria.getCheckInDate() != null && criteria.getCheckInDate().getSortingType().equals(SortingType.ASC)) {
                query.orderBy(cb.asc(root.get("checkIn")));
            } else if (criteria.getCheckInDate() != null && criteria.getCheckInDate().getSortingType().equals(SortingType.DESC)) {
                query.orderBy(cb.desc(root.get("checkIn")));
            }

            if (criteria.getCheckOutDate() != null && criteria.getCheckOutDate().getSortingType().equals(SortingType.ASC)) {
                query.orderBy(cb.asc(root.get("checkOut")));
            } else if (criteria.getCheckOutDate() != null && criteria.getCheckOutDate().getSortingType().equals(SortingType.DESC)) {
                query.orderBy(cb.desc(root.get("checkOut")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return bookingRepository.findAll(spec, pr);
    }

    @Override
    public BookingDataResponseModel updateBooking(String bookingId, UpdateBookingOrderRequestModel updateBookingOrderRequestModel) {
        log.info("Updating the booking details for bookingId: {} with data:{}", bookingId, updateBookingOrderRequestModel);

        log.info("Fetching the previous data");
        Optional<BookingDetails> bookingDetails = bookingRepository.findById(bookingId);
        if(bookingDetails.isEmpty()){
            log.error("Could not find any booking for bookingId:{}", bookingId);
            throw new BookingNotFoundException(ApiConstants.NOT_FOUND, "Could not find any booking for bookingId: "+bookingId, null);
        }
        log.info("Fetched booking data:{}", bookingDetails.get());

        BookingDetails booking = bookingDetails.get();

        try{
            BookingDetails updatedBookingData = updateTheBookingDetailsWithNewData(booking, updateBookingOrderRequestModel);
            BookingDetails savedData = bookingRepository.save(updatedBookingData);
            log.info("Updated booking: {}", savedData);
            return bookingMapper.toBookingDataResponseModel(savedData);
        }catch (Exception ex){
            log.info("Some error occurred while updating the booking details: {}", ex.getMessage());
            throw new BookingServiceRuntimeException(ex.getMessage());
        }
    }

    private BookingDetails updateTheBookingDetailsWithNewData(BookingDetails booking, UpdateBookingOrderRequestModel updateBookingOrderRequestModel) {
        if(updateBookingOrderRequestModel.getStatus() != null){
            booking.setStatus(updateBookingOrderRequestModel.getStatus());
        }
        return booking;
    }

    private void updateStatus(BookingDetails bookingDetails, BookingStatus bookingStatus) {
        log.info("Changing status to PAY_AT_HOTEL");
        bookingDetails.setStatus(BookingStatus.PAY_AT_HOTEL);
        log.info("Saving booking record");
        bookingDetails = bookingRepository.save(bookingDetails);
        log.info("Saved booking record");
    }

    private OnlinePaymentOrderResponseDto doOnlinePayment(BookingDetails bookingDetails) {
        log.info("Creating booking payment order.");
        switch (bookingDetails.getPaymentMetaDataModel().getPaymentMethodType()){
            case STRIPE : {
                log.info("Redirecting to {} payment service provider for payment order , as the payment method is: {}",
                        bookingDetails.getPaymentMetaDataModel().getPaymentMethodType(),
                        bookingDetails.getPaymentMetaDataModel().getPaymentMethodType()
                );
                StripePaymentOrderResponseDto stripePaymentOrderResponseDto = createStripePaymentOrder(bookingDetails);
                OnlinePaymentOrderResponseDto onlinePaymentOrderResponseDto = bookingMapper.toOnlinePaymentOrderResponseDto(stripePaymentOrderResponseDto);
                log.info("Created booking payment order: {}", onlinePaymentOrderResponseDto);
                return onlinePaymentOrderResponseDto;
            }

            default: {
                log.info("Booking cannot be processed due to invalid payment method type: {}", bookingDetails.getPaymentMetaDataModel().getPaymentMethodType());
                throw new InvalidPaymentMethodTypeException(ApiConstants.INVALID_PAYMENT_METHOD_TYPE, "Invalid payment method type", "");
            }
        }
    }

    private StripePaymentOrderResponseDto createStripePaymentOrder(BookingDetails bookingDetails) {
        PaymentOrderRequestDto paymentOrderRequestDto = bookingMapper.toPaymentOrderRequestDto(bookingDetails);

        log.info("Creating {} payment order for: {}", bookingDetails.getPaymentMetaDataModel().getPaymentMethodType(), paymentOrderRequestDto);
        PaymentOrderResponseDto paymentOrderResponseDto = paymentService.createPaymentOrder(paymentOrderRequestDto);
        log.info("Created {} payment order: {}", bookingDetails.getPaymentMetaDataModel().getPaymentMethodType(), paymentOrderResponseDto);

        updateBookingDetails(bookingDetails, paymentOrderResponseDto);
        log.info("Updated bookingDetails with {} data", bookingDetails.getPaymentMetaDataModel().getPaymentMethodType());

        StripePaymentOrderResponseDto stripePaymentOrderResponseDto = bookingMapper.toStripePaymentOrderResponseDto(paymentOrderResponseDto);

        return stripePaymentOrderResponseDto;
    }

    private void updateBookingDetails(BookingDetails bookingDetails, PaymentOrderResponseDto paymentOrderResponseDto) {
        log.info("Updating bookingDetails with {} Payment Service Provider data", bookingDetails.getPaymentMetaDataModel().getPaymentMethodType());
        StripePaymentServiceProvider stripePaymentServiceProvider = bookingMapper.toStripePaymentServiceProvider(paymentOrderResponseDto);
        BookingPaymentMetaDataModel bookingPaymentMetaDataModel = bookingDetails.getPaymentMetaDataModel();
        bookingPaymentMetaDataModel.setStripePaymentServiceProvider(stripePaymentServiceProvider);
        try{
            bookingRepository.updatePaymentMetaDataModelById(bookingDetails.getId(), bookingPaymentMetaDataModel);
            log.info("Updated booking details with payment service provider");
        }catch (Exception ex){
            log.info("Some error occurred while updating the booking details: {}", ex.getMessage());
            throw new BookingServiceRuntimeException(ex.getMessage());
        }
    }

    private BookingDetails createBookingRecord(BookingOrderRequestModel bookingOrderRequestModel, BookingRequestDetails bookingRequestDetails) {
        log.info("creating booking record");

        RoomDetailsDto roomDetailsDto = roomService.getRoom(bookingRequestDetails.getRoomId());
        String amount = roomMapper.toAmount(roomDetailsDto);
        RoomMetaDataModel roomMetaDataModel = roomMapper.toRoomMetaDataModel(roomDetailsDto);
        BookingPaymentMetaDataModel bookingPaymentMetaDataModel = roomMapper.toBookingPaymentMetaDataModel(bookingRequestDetails.getPaymentMethodType(), amount, roomMetaDataModel.getPrices().getCurrency(), null);
        BookingStatus status = BookingStatus.PENDING_PAYMENT;
        Instant bookingDate = Instant.now();

        try {
            BookingDetails bookingDetails = createBookingDetails(
                    bookingRequestDetails,
                    amount,
                    roomMetaDataModel,
                    bookingPaymentMetaDataModel,
                    status,
                    bookingDate
            );

            bookingDetails = bookingRepository.save(bookingDetails);

            log.info("created booking record: {}", bookingDetails);

            return bookingDetails;
        }catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new BookingServiceRuntimeException(ex.getMessage());
        }
    }

    private BookingDetails createBookingDetails(BookingRequestDetails bookingRequestDetails,
                                                String amount,
                                                RoomMetaDataModel roomMetaDetails,
                                                BookingPaymentMetaDataModel bookingPaymentMetaDataModel,
                                                BookingStatus status,
                                                Instant bookingDate) {
        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setBookingRequestId(bookingRequestDetails.getId());
        bookingDetails.setOrganizationId(bookingRequestDetails.getOrganizationId());
        bookingDetails.setCheckIn(Instant.parse(bookingRequestDetails.getCheckIn()));
        bookingDetails.setCheckOut(Instant.parse(bookingRequestDetails.getCheckOut()));
        bookingDetails.setPeopleCount((long) (bookingRequestDetails.getGuests().getAdults() + bookingRequestDetails.getGuests().getChildren()));
        bookingDetails.setGuests(bookingRequestDetails.getGuests());
        bookingDetails.setContactDetails(bookingRequestDetails.getContactDetails());
        bookingDetails.setPaymentType(bookingRequestDetails.getPaymentType());
        bookingDetails.setUid(bookingRequestDetails.getUid());
        bookingDetails.setBookingDate(bookingDate);
        bookingDetails.setAmount(amount);
        bookingDetails.setRoomDetails(roomMetaDetails);
        bookingDetails.setPaymentMetaDataModel(bookingPaymentMetaDataModel);
        bookingDetails.setStatus(status);
        return bookingDetails;
    }

    private void validateCurrentUser(String currentUserUid, String prevUserUid){
        log.info("Validating current user is the one who created the booking request");
        if(currentUserUid.equals(prevUserUid) == false) {
            log.error("Unauthorized access: Current user '{}' is not the creator of the booking request", currentUserUid);
            throw new BookingRequestUnauthorizedAccessException(ApiConstants.UNAUTHORIZED, "You do not have permission to modify this booking request.", "");
        }
        log.info("Validated current user");
    }
}