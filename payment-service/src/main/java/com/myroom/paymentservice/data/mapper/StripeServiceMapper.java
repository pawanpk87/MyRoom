package com.myroom.paymentservice.data.mapper;

import com.myroom.paymentservice.data.dto.StripeCreatePriceObjectDto;
import com.myroom.paymentservice.data.dto.StripeCreateSessionObjectDto;
import com.myroom.paymentservice.data.dto.StripePaymentOrder;
import com.myroom.paymentservice.data.dto.StripePaymentOrderRequestModel;
import com.myroom.paymentservice.util.Utils;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StripeServiceMapper {
    public StripeCreatePriceObjectDto toStripeCreatePriceObjectDto(StripeCreateSessionObjectDto stripeCreateSessionObjectDto) {
        return StripeCreatePriceObjectDto.builder()
                .bookingId(stripeCreateSessionObjectDto.getBookingId())
                .roomId(stripeCreateSessionObjectDto.getRoomId())
                .title(stripeCreateSessionObjectDto.getTitle())
                .uid(stripeCreateSessionObjectDto.getUid())
                .amount(stripeCreateSessionObjectDto.getAmount())
                .currency(stripeCreateSessionObjectDto.getCurrency())
                .receipt(stripeCreateSessionObjectDto.getReceipt())
                .organizationId(stripeCreateSessionObjectDto.getOrganizationId())
                .build();
    }

    public PriceCreateParams toPriceCreateParams(StripeCreatePriceObjectDto stripeCreatePriceObjectDto) {
        String currency = stripeCreatePriceObjectDto.getCurrency();
        BigDecimal amount = stripeCreatePriceObjectDto.getAmount();
        String roomId = stripeCreatePriceObjectDto.getRoomId();
        String title = stripeCreatePriceObjectDto.getTitle();
        String uid = stripeCreatePriceObjectDto.getUid();

        return PriceCreateParams.builder()
                .setCurrency(currency)
                .setUnitAmount(Utils.getSubunitsAmountForINR(amount.longValue()))
                .setNickname(title)
                .setProductData(
                        PriceCreateParams.ProductData.builder()
                                .setName(title)
                                .build()
                )
                .build();
    }

    public SessionCreateParams toSessionCreateParams( StripeCreateSessionObjectDto stripeCreateSessionObjectDto,
                                                      Price price,
                                                      SessionCreateParams.PaymentIntentData paymentIntentData,
                                                      String successUrl,
                                                      String cancelUrl) {
        String roomId = stripeCreateSessionObjectDto.getRoomId();
        String bookingId = stripeCreateSessionObjectDto.getBookingId();
        String currency = stripeCreateSessionObjectDto.getCurrency();
        String uid = stripeCreateSessionObjectDto.getUid();

        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCurrency(currency)
                .setClientReferenceId(bookingId)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPrice(price.getId())
                                .build()
                )
                .setPaymentIntentData(paymentIntentData)
                .build();
    }

    public StripePaymentOrder toStripePaymentOrder(Session session, StripePaymentOrderRequestModel stripePaymentOrderRequestModel) {
        return StripePaymentOrder.builder()
                .id(session.getId())
                .url(session.getUrl())
                .status(session.getStatus())
                .amount(String.valueOf(session.getAmountTotal()))
                .build();
    }
}