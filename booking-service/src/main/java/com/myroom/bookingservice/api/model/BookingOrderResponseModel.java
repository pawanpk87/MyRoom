package com.myroom.bookingservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.bookingservice.api.constants.BookingStatus;
import com.myroom.bookingservice.api.constants.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingOrderResponseModel {
    @Schema(description = "success", example = "true", required = true)
    boolean success;

    @Schema(description = "payment type", example = "ONLINE_PAYMENT", required = true)
    PaymentType paymentType;

    @Schema(description = "bookingId", example = "bk_kjnas345fdfuh3kjnasd8o34nt")
    String bookingId;

    @Schema(description = "booking status", example = "CONFIRMED", required = true)
    BookingStatus status;

    @Schema(description = "paymentOrderId", example = "order_JJCYnu3hipocHz", required = true)
    String paymentOrderId;

    @Schema(description = "amount", example = "5000", required = true)
    String amount;

    @Schema(description = "url", example = "https://checkout.stripe.com/c/pay/cs_test_a1UefKa0q1QkKyyYsuJ...")
    String url;
}