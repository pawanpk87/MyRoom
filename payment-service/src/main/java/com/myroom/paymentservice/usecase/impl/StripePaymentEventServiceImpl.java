package com.myroom.paymentservice.usecase.impl;

import com.myroom.paymentservice.api.constants.BookingStatus;
import com.myroom.paymentservice.data.dto.BookingDataResponseDto;
import com.myroom.paymentservice.data.dto.UpdateRoomStatusPayload;
import com.myroom.paymentservice.exception.PaymentServiceRuntimeException;
import com.myroom.paymentservice.repository.PaymentDetailsRepository;
import com.myroom.paymentservice.usecase.*;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StripePaymentEventServiceImpl implements StripePaymentEventService {
    @Autowired
    PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    PaymentService paymentService;

    @Autowired
    BookingService bookingService;

    @Autowired
    RoomService roomService;

    @Autowired
    KafkaMessageService kafkaMessageService;


    @Override
    public void handlePaymentCompletedUpdatedEvent(StripeObject stripeObject) {
        log.info("Received checkout.session.completed event with data:{}", stripeObject);
        Session sessionObj = (Session) stripeObject;

        String paymentId = sessionObj.getId();
        String status = sessionObj.getStatus();
        String bookingId = sessionObj.getClientReferenceId();

        try {
            log.info("Updating payment status");
            paymentDetailsRepository.updateStatusByPaymentId(paymentId, status);
            log.info("Updated payment status");

            updateRoomStatus(bookingId);
        }catch (Exception ex){
            log.error("Some error occurred while updating the payment status: {}", ex.getMessage());
            throw new PaymentServiceRuntimeException(ex.getMessage());
        }

        triggerBookingPaymentCompletedEvent(bookingId, status);
    }

    private void triggerBookingPaymentCompletedEvent(String bookingId, String status) {
        log.info("triggering booking payment completed event (kafka topic: 'booking.payment' & 'booking.mail')");
        JSONObject messagePayload = new JSONObject();
        messagePayload.put("bookingId", bookingId);
        messagePayload.put("status", status);
        kafkaMessageService.sendMessage("booking.payment", messagePayload.toString());
        kafkaMessageService.sendMessage("booking.mail", messagePayload.toString());
    }

    private void updateRoomStatus(String bookingId){
        log.info("Updating room status");
        BookingDataResponseDto bookingDataResponseDto = bookingService.getBookingDetails(bookingId);

        UpdateRoomStatusPayload updateRoomStatusPayload = UpdateRoomStatusPayload.builder()
                .id(bookingDataResponseDto.getRoomDetails().getId())
                .bookingStatus(BookingStatus.OCCUPIED)
                .uid(bookingDataResponseDto.getUid())
                .build();

        roomService.updateRoomStatus(updateRoomStatusPayload);

        log.info("Updated the room status");
    }
}