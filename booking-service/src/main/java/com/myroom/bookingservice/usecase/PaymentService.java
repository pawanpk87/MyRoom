package com.myroom.bookingservice.usecase;

import com.myroom.bookingservice.data.dto.PaymentOrderRequestDto;
import com.myroom.bookingservice.data.dto.PaymentOrderResponseDto;

public interface PaymentService {
    PaymentOrderResponseDto createPaymentOrder(PaymentOrderRequestDto paymentOrderRequestDto);
}