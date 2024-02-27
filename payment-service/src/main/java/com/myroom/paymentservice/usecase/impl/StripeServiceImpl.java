package com.myroom.paymentservice.usecase.impl;

import com.google.gson.JsonSyntaxException;
import com.myroom.paymentservice.api.constants.ApiConstants;
import com.myroom.paymentservice.data.dto.StripeCreatePriceObjectDto;
import com.myroom.paymentservice.data.dto.StripeCreateSessionObjectDto;
import com.myroom.paymentservice.data.mapper.StripeServiceMapper;
import com.myroom.paymentservice.exception.InvalidPriceException;
import com.myroom.paymentservice.exception.PaymentServiceRuntimeException;
import com.myroom.paymentservice.usecase.OrganizationPayService;
import com.myroom.paymentservice.usecase.PaymentService;
import com.myroom.paymentservice.usecase.StripePaymentEventService;
import com.myroom.paymentservice.usecase.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Price;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class StripeServiceImpl implements StripeService {
    @Autowired
    OrganizationPayService organizationPayService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    StripePaymentEventService stripePaymentEventService;

    @Autowired
    StripeServiceMapper stripeServiceMapper;

    @Value("${stripe.api.secret-key}")
    String SecretKey;

    @Value("${stripe.endpointSecret}")
    String endpointSecret;

    @PostConstruct
    private void initialize(){
        // set the stripe secret api key
        Stripe.apiKey = SecretKey;
    }

    @Override
    public Session createSession(StripeCreateSessionObjectDto stripeCreateSessionObjectDto){
        log.info("Creating Stripe Session Object for: {}", stripeCreateSessionObjectDto);

        try {
            Price price = createPrice(stripeServiceMapper.toStripeCreatePriceObjectDto(stripeCreateSessionObjectDto));
            SessionCreateParams.PaymentIntentData paymentIntentData = createPaymentIntentData(stripeCreateSessionObjectDto);
            String successUrl = paymentService.getSuccessUrl(stripeCreateSessionObjectDto.getBookingId());
            String cancelUrl = paymentService.getCancelUrl(stripeCreateSessionObjectDto.getBookingId());

            SessionCreateParams params = stripeServiceMapper.toSessionCreateParams(
                    stripeCreateSessionObjectDto,
                    price,
                    paymentIntentData,
                    successUrl,
                    cancelUrl
            );

            Session session = Session.create(params);

            log.info("Stripe Session Object created successfully: {}", session);

            return session;
        } catch (StripeException stripeException){
            log.error("Could not create stripe session: {}", stripeException.getMessage());
            throw new InvalidPriceException(ApiConstants.INVALID_PRICE, ApiConstants.MESSAGE_INVALID_PRICE, stripeException.getMessage());
        }
    }

    @Override
    public void handleEvent(String payload, Map<String, String> headers) {
        log.info("Received Stripe Webhook event with payload: {} and headers: {}", payload, headers);
        Event event = getEventObject(payload, headers);
        StripeObject stripeObject = getStripeObject(event);
        log.info("Handling event");
        switch (event.getType()){
            case "checkout.session.completed":{
                stripePaymentEventService.handlePaymentCompletedUpdatedEvent(stripeObject);
                break;
            }
            //TODO: handle 'checkout.session.expired', 'checkout.session.async_payment_failed', 'checkout.session.async_payment_succeeded'
            default:

        }
    }

    private Price createPrice(StripeCreatePriceObjectDto stripeCreatePriceObjectDto){
        log.info("Creating stripe Price Object");

        try {
            PriceCreateParams params = stripeServiceMapper.toPriceCreateParams(stripeCreatePriceObjectDto);
            Price price = Price.create(params);

            log.info("Price Object created successfully: {}", price);

            return price;
        }catch (StripeException stripeException){
            log.error("Could not create price: {}", stripeException.getMessage());
            throw new InvalidPriceException(ApiConstants.INVALID_PRICE, stripeException.getMessage(), stripeException.getMessage());
        }
    }

    private SessionCreateParams.PaymentIntentData createPaymentIntentData(StripeCreateSessionObjectDto stripeCreateSessionObjectDto){
        log.info("Creating Stripe PaymentIntentData object");
        String roomOrganizationStripeAccountId = organizationPayService.getOrganizationStripeAccountId(stripeCreateSessionObjectDto.getOrganizationId());
        Long myRoomOrganizationApplicationFeeAmount = paymentService.getApplicationFeeAmount(stripeCreateSessionObjectDto.getAmount());
        SessionCreateParams.PaymentIntentData data = SessionCreateParams.PaymentIntentData.builder()
                .setApplicationFeeAmount(myRoomOrganizationApplicationFeeAmount)
                .setTransferData(
                        SessionCreateParams.PaymentIntentData.TransferData.builder()
                                .setDestination(roomOrganizationStripeAccountId)
                                .build()
                )
                .build();
        log.info("PaymentIntentData Object created successfully: {}", data);
        return data;
    }

    private StripeObject getStripeObject(Event event) {
        log.info("Fetching StripeObject from event");
        log.info("Deserializing the nested object inside event");
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if(dataObjectDeserializer.getObject().isPresent()){
            StripeObject stripeObject = dataObjectDeserializer.getObject().get();
            log.info("Fetched StripeObject: {}", stripeObject);
            return stripeObject;
        }else{
            log.error("Deserialization failed");
            throw new PaymentServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }

    private Event getEventObject(String payload, Map<String, String> headers) {
        log.info("Fetching event object from stipe webhook event");
        String sigHeader = headers.get("stripe-signature");
        try {
            Event event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
            log.info("Fetched event object: {}", event);
            return event;
        } catch (JsonSyntaxException ex){
            log.error("Invalid payload");
            throw new PaymentServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        } catch (SignatureVerificationException ex){
            log.error("Invalid signature");
            throw new PaymentServiceRuntimeException(ApiConstants.MESSAGE_INTERNAL_SERVER_ERROR);
        }
    }
}