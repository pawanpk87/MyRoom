package com.myroom.paymentservice.usecase.impl;

import com.myroom.paymentservice.data.dto.BookingDataResponseDto;
import com.myroom.paymentservice.exception.PaymentServiceRuntimeException;
import com.myroom.paymentservice.usecase.BookingService;
import com.myroom.paymentservice.util.EurekaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Controller
@Slf4j
public class BookingServiceImpl implements BookingService {
    @Autowired
    EurekaClientUtil eurekaClientUtil;

    @Autowired
    WebClient webClient;

    @Value("${services.booking-service.id}")
    String bookingServiceID;

    @Value("${services.booking-service.v1.api}")
    String bookingServiceV1;

    @Value("${services.booking-service.v1.name}")
    String bookingServiceV1Name;


    @Override
    public BookingDataResponseDto getBookingDetails(String bookingId) {
        log.info("calling Booking Service for getting booking details");

        BookingDataResponseDto bookingDataResponseDto = null;

        // Construct URL
        String bookingServiceUri = eurekaClientUtil.getServiceUri(bookingServiceID);
        String url = bookingServiceUri + bookingServiceV1 + "/" + bookingId;

        try {
            BookingDataResponseDto response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(BookingDataResponseDto.class)
                    .block();

            log.info("{} response: {}", bookingServiceV1Name, response);

            bookingDataResponseDto = response;
        }catch (WebClientResponseException webClientResponseException){
            handleWebClientException(webClientResponseException);
        }catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new PaymentServiceRuntimeException(ex.getMessage());
        }

        return bookingDataResponseDto;
    }

    private void handleWebClientException(WebClientResponseException webClientResponseException) {
        log.info("Handling WebClientException");

        // Convert error response to JSONObject
        JSONObject response = new JSONObject(webClientResponseException.getResponseBodyAsString());

        log.info("Error response from {} Service is: {}", bookingServiceV1Name, response);

        String errorCode = response.getString("errorCode");
        String message = response.getString("message");
        String details = response.has("details") ? response.getString("details") : "";

        throw new PaymentServiceRuntimeException(message);
    }
}
