package com.myroom.bookingservice.data.mapper;

import com.myroom.bookingservice.api.constants.PaymentMethodType;
import com.myroom.bookingservice.api.constants.PaymentType;
import com.myroom.bookingservice.api.model.BookingDataResponseModel;
import com.myroom.bookingservice.api.model.BookingOrderResponseModel;
import com.myroom.bookingservice.data.dto.OnlinePaymentOrderResponseDto;
import com.myroom.bookingservice.data.dto.PaymentOrderRequestDto;
import com.myroom.bookingservice.data.dto.PaymentOrderResponseDto;
import com.myroom.bookingservice.data.dto.StripePaymentOrderResponseDto;
import com.myroom.bookingservice.data.entity.BookingDetails;
import com.myroom.bookingservice.data.model.StripePaymentServiceProvider;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    public BookingOrderResponseModel toBookingOrderResponseModel(OnlinePaymentOrderResponseDto onlinePaymentOrderResponseDto, BookingDetails bookingDetails) {
        BookingOrderResponseModel bookingOrderResponseModel = new BookingOrderResponseModel();
        bookingOrderResponseModel.setSuccess(true);
        bookingOrderResponseModel.setPaymentType(PaymentType.ONLINE_PAYMENT);
        bookingOrderResponseModel.setBookingId(bookingDetails.getId());
        bookingOrderResponseModel.setPaymentOrderId(onlinePaymentOrderResponseDto.getId());
        bookingOrderResponseModel.setStatus(bookingDetails.getStatus());
        bookingOrderResponseModel.setAmount(onlinePaymentOrderResponseDto.getAmount());
        bookingOrderResponseModel.setUrl(onlinePaymentOrderResponseDto.getUrl());
        return bookingOrderResponseModel;
    }

    public BookingOrderResponseModel toBookingOrderResponseModel(BookingDetails bookingDetails) {
        BookingOrderResponseModel bookingOrderResponseModel = new BookingOrderResponseModel();
        bookingOrderResponseModel.setSuccess(true);
        bookingOrderResponseModel.setPaymentType(bookingDetails.getPaymentType());
        bookingOrderResponseModel.setStatus(bookingDetails.getStatus());
        bookingOrderResponseModel.setBookingId(bookingOrderResponseModel.getBookingId());
        if(bookingDetails.getPaymentType().equals(PaymentType.ONLINE_PAYMENT)){
            if(bookingDetails.getPaymentMetaDataModel().getPaymentMethodType().equals(PaymentMethodType.STRIPE)){
                StripePaymentServiceProvider stripePaymentServiceProvider =  bookingDetails.getPaymentMetaDataModel().getStripePaymentServiceProvider();
                bookingOrderResponseModel.setPaymentOrderId(stripePaymentServiceProvider.getId());
                bookingOrderResponseModel.setAmount(stripePaymentServiceProvider.getAmount());
                bookingOrderResponseModel.setUrl(stripePaymentServiceProvider.getUrl());
            }else{

            }
        }
        return bookingOrderResponseModel;
    }

    public OnlinePaymentOrderResponseDto toOnlinePaymentOrderResponseDto(StripePaymentOrderResponseDto stripePaymentOrderResponseDto) {
        return OnlinePaymentOrderResponseDto.builder()
                .id(stripePaymentOrderResponseDto.getId())
                .amount(stripePaymentOrderResponseDto.getAmount())
                .status(stripePaymentOrderResponseDto.getStatus())
                .url(stripePaymentOrderResponseDto.getUrl())
                .build();
    }

    public PaymentOrderRequestDto toPaymentOrderRequestDto(BookingDetails bookingDetails) {
        return PaymentOrderRequestDto.builder()
                .paymentMethodType(bookingDetails.getPaymentMetaDataModel().getPaymentMethodType())
                .amount(bookingDetails.getAmount())
                .currency(bookingDetails.getPaymentMetaDataModel().getCurrency())
                .receipt("#bookingId: "+bookingDetails.getId())
                .organizationId(bookingDetails.getRoomDetails().getOrganizationId())
                .roomId(bookingDetails.getRoomDetails().getId())
                .bookingId(bookingDetails.getId())
                .roomTitle(bookingDetails.getRoomDetails().getTitle())
                .uid(bookingDetails.getUid())
                .build();
    }

    public StripePaymentServiceProvider toStripePaymentServiceProvider(PaymentOrderResponseDto paymentOrderResponseDto) {
        return StripePaymentServiceProvider.builder()
                .id(paymentOrderResponseDto.getId())
                .amount(paymentOrderResponseDto.getAmount())
                .status(StripePaymentServiceProvider.StripePaymentStatus.valueOf(paymentOrderResponseDto.getStatus()))
                .url(paymentOrderResponseDto.getUrl())
                .build();
    }

    public StripePaymentOrderResponseDto toStripePaymentOrderResponseDto(PaymentOrderResponseDto paymentOrderResponseDto) {
        return StripePaymentOrderResponseDto.builder()
                .id(paymentOrderResponseDto.getId())
                .amount(paymentOrderResponseDto.getAmount())
                .status(paymentOrderResponseDto.getStatus())
                .url(paymentOrderResponseDto.getUrl())
                .build();
    }

    public BookingDataResponseModel toBookingDataResponseModel(BookingDetails bookingDetails) {
        return BookingDataResponseModel.builder()
                .id(bookingDetails.getId())
                .bookingRequestId(bookingDetails.getBookingRequestId())
                .amount(bookingDetails.getAmount())
                .checkIn(bookingDetails.getCheckIn().toString())
                .checkOut(bookingDetails.getCheckOut().toString())
                .roomDetails(bookingDetails.getRoomDetails())
                .paymentMetaDataModel(bookingDetails.getPaymentMetaDataModel())
                .status(bookingDetails.getStatus())
                .guests(bookingDetails.getGuests())
                .contactDetails(bookingDetails.getContactDetails())
                .paymentType(bookingDetails.getPaymentType())
                .bookingDate(bookingDetails.getBookingDate().toString())
                .uid(bookingDetails.getUid())
                .createdAt(bookingDetails.getCreatedAt().toString())
                .updatedAt(bookingDetails.getUpdatedAt().toString())
                .build();
    }
}