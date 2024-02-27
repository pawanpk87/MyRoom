package com.myroom.paymentservice.data.mapper;

import com.myroom.paymentservice.api.model.*;
import com.myroom.paymentservice.data.dto.StripePaymentOrderRequestModel;
import com.myroom.paymentservice.data.dto.StripePaymentOrderResponseModel;
import com.myroom.paymentservice.data.dto.StripePaymentOrderSuccessRequest;
import com.myroom.paymentservice.data.dto.StripePaymentOrderSuccessResponse;
import com.myroom.paymentservice.data.entity.PaymentDetails;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public StripePaymentOrderRequestModel toStripePaymentOrderRequestModel(PaymentOrderRequestModel paymentOrderRequestModel) {
        return StripePaymentOrderRequestModel.builder()
                .paymentMethodType(paymentOrderRequestModel.getPaymentMethodType())
                .bookingId(paymentOrderRequestModel.getBookingId())
                .roomId(paymentOrderRequestModel.getRoomId())
                .roomTitle(paymentOrderRequestModel.getRoomTitle())
                .uid(paymentOrderRequestModel.getUid())
                .amount(paymentOrderRequestModel.getAmount())
                .currency(paymentOrderRequestModel.getCurrency())
                .receipt(paymentOrderRequestModel.getReceipt())
                .organizationId(paymentOrderRequestModel.getOrganizationId())
                .build();
    }

    public PaymentOrderResponseModel toPaymentOrderResponseModel(StripePaymentOrderResponseModel stripePaymentOrderResponseModel) {
        return PaymentOrderResponseModel.builder()
                .id(stripePaymentOrderResponseModel.getId())
                .url(stripePaymentOrderResponseModel.getUrl())
                .status(stripePaymentOrderResponseModel.getStatus())
                .amount(stripePaymentOrderResponseModel.getAmount())
                .build();
    }

    public StripePaymentOrderSuccessRequest toRazorpayPaymentOrderSuccessRequest(PaymentOrderSuccessRequest paymentOrderSuccessRequest) {
        return StripePaymentOrderSuccessRequest.builder()
                .paymentMethodType(paymentOrderSuccessRequest.getPaymentMethodType())
                .orderCreationId(paymentOrderSuccessRequest.getOrderCreationId())
                .razorpayPaymentId(paymentOrderSuccessRequest.getPaymentServiceProviderPaymentId())
                .razorpayOrderId(paymentOrderSuccessRequest.getPaymentServiceProviderOrderId())
                .razorpaySignature(paymentOrderSuccessRequest.getPaymentServiceProviderSignature())
                .build();
    }

    public PaymentOrderSuccessResponse toPaymentOrderSuccessResponse(StripePaymentOrderSuccessResponse razorpayPaymentOrderSuccessResponse) {
        return PaymentOrderSuccessResponse.builder()
                .message(razorpayPaymentOrderSuccessResponse.getMessage())
                .build();
    }

    public PaymentDetailsResponseModel toPaymentDetailsResponseModel(PaymentDetails paymentDetails) {
        return PaymentDetailsResponseModel.builder()
                .id(paymentDetails.getId())
                .type(paymentDetails.getType())
                .amount(paymentDetails.getAmount())
                .currency(paymentDetails.getCurrency())
                .bookingId(paymentDetails.getBookingId())
                .status(paymentDetails.getStatus())
                .createdAt(paymentDetails.getCreatedAt())
                .build();
    }
}
