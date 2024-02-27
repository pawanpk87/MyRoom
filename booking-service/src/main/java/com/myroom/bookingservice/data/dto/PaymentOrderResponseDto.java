package com.myroom.bookingservice.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderResponseDto {
    private String id;

    private String amount;

    private String status;

    private String url;
}