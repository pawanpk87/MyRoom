package com.myroom.paymentservice.usecase;

import com.myroom.paymentservice.data.dto.StripePaymentOrderRequestModel;
import com.myroom.paymentservice.data.dto.StripePaymentOrderResponseModel;
import com.myroom.paymentservice.data.dto.StripePaymentOrderSuccessRequest;
import com.myroom.paymentservice.data.dto.StripePaymentOrderSuccessResponse;

public interface StripePaymentService {
    StripePaymentOrderResponseModel createOrder(StripePaymentOrderRequestModel razorpayPaymentOrderRequestModel);

    StripePaymentOrderSuccessResponse handlePaymentOrderSuccess(StripePaymentOrderSuccessRequest razorpayPaymentOrderSuccessRequest);
}
