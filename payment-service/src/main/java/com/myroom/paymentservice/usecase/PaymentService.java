package com.myroom.paymentservice.usecase;

import com.myroom.paymentservice.api.model.*;
import com.myroom.paymentservice.api.model.dashboard.PaymentStatisticsResponseModel;
import com.myroom.paymentservice.api.model.dashboard.RevenueStatisticsResponseModel;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentOrderResponseModel createOrder(PaymentOrderRequestModel paymentOrderRequestModel);

    PaymentOrderSuccessResponse handlePaymentOrderSuccess(PaymentOrderSuccessRequest paymentOrderSuccessRequest);

    public Long getApplicationFeeAmount(BigDecimal amount);

    String getSuccessUrl(String bookingId);

    public String getCancelUrl(String bookingId);

    PaymentDetailsResponseModel getPaymentDetailsByBookingId(String bookingId);

    PaymentStatisticsResponseModel getBookingStatistics(String organizationId, String startDate, String endDate);

    RevenueStatisticsResponseModel getRevenueStatistics(String organizationId, String duration);
}