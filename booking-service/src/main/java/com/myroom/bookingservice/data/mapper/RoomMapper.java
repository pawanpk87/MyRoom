package com.myroom.bookingservice.data.mapper;

import com.myroom.bookingservice.api.constants.PaymentMethodType;
import com.myroom.bookingservice.data.dto.RoomDetailsDto;
import com.myroom.bookingservice.data.dto.RoomPricesDto;
import com.myroom.bookingservice.data.model.BookingPaymentMetaDataModel;
import com.myroom.bookingservice.data.model.RoomMetaDataModel;
import com.myroom.bookingservice.data.model.RoomPriceMetaDataModel;
import com.myroom.bookingservice.data.model.StripePaymentServiceProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RoomMapper {
    public RoomMetaDataModel toRoomMetaDataModel(RoomDetailsDto roomDetails) {
        RoomMetaDataModel roomMetaDataModel = new RoomMetaDataModel();
        roomMetaDataModel.setId(roomDetails.getId());
        roomMetaDataModel.setTitle(roomDetails.getTitle());
        roomMetaDataModel.setOrganizationId(roomDetails.getOrganizationId());
        roomMetaDataModel.setPrices(toRoomPriceMetaDataModel(roomDetails));
        return roomMetaDataModel;
    }

    private RoomPriceMetaDataModel toRoomPriceMetaDataModel(RoomDetailsDto roomDetails) {
        RoomPriceMetaDataModel roomPriceMetaDataModel = new RoomPriceMetaDataModel();
        roomPriceMetaDataModel.setPrice(roomDetails.getPrices().getPrice());
        roomPriceMetaDataModel.setCleaningFee(roomDetails.getPrices().getCleaningFee());
        roomPriceMetaDataModel.setRoomService(roomDetails.getPrices().getRoomService());
        roomPriceMetaDataModel.setCurrency(roomDetails.getPrices().getCurrency());
        return roomPriceMetaDataModel;
    }

    public String toAmount(RoomDetailsDto roomDetails) {
        BigDecimal amount = new BigDecimal(0);
        RoomPricesDto roomPrices = roomDetails.getPrices();
        amount = amount.add(new BigDecimal(roomPrices.getPrice()));
        amount = amount.add(new BigDecimal(roomPrices.getCleaningFee()));
        amount = amount.add(new BigDecimal(roomPrices.getRoomService()));
        return amount.toPlainString();
    }

    public BookingPaymentMetaDataModel toBookingPaymentMetaDataModel(PaymentMethodType paymentMethodType, String amount, String currency, StripePaymentServiceProvider stripePaymentServiceProvider) {
        return BookingPaymentMetaDataModel.builder()
                .paymentMethodType(paymentMethodType)
                .amount(amount)
                .currency(currency)
                .stripePaymentServiceProvider(stripePaymentServiceProvider)
                .build();
    }
}