package com.myroom.bookingservice.usecase.impl;

import com.myroom.bookingservice.api.constants.ApiConstants;
import com.myroom.bookingservice.data.dto.RoomAvailabilityRequest;
import com.myroom.bookingservice.data.dto.RoomAvailabilityResponse;
import com.myroom.bookingservice.data.dto.RoomDetailsDto;
import com.myroom.bookingservice.exception.*;
import com.myroom.bookingservice.usecase.RoomService;
import com.myroom.bookingservice.utils.EurekaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService{
    @Autowired
    EurekaClientUtil eurekaClientUtil;

    @Autowired
    WebClient webClient;

    @Value("${services.room-service.id}")
    String roomServiceID;

    @Value("${services.room-service.v1.api}")
    String roomServiceV1;

    @Value("${services.room-service.v1.name}")
    String roomServiceV1Name;


    @Override
    public boolean checkAvailability(RoomAvailabilityRequest roomAvailabilityRequest) {
        log.info("calling {} Service for checking room availability for: {}", roomServiceV1Name, roomAvailabilityRequest);

        Boolean roomAvailability = false;

        // Construct URL
        String roomServiceUri = eurekaClientUtil.getServiceUri(roomServiceID);
        String url = roomServiceUri + roomServiceV1 + "/" + roomAvailabilityRequest.getRoomId() + "/availability";

        try {
            RoomAvailabilityResponse response = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(roomAvailabilityRequest)
                    .retrieve()
                    .bodyToMono(RoomAvailabilityResponse.class)
                    .block();

            log.info("Room service response: {}", response);

            if(response.isAvailable() == false){
                log.info("Room is unavailable");
                throw new RoomUnavailableException(ApiConstants.ROOM_UNAVAILABLE, response.getMessage(), "");
            }

            roomAvailability = true;
        } catch (WebClientResponseException webClientResponseException){
             handleWebClientException(webClientResponseException);
        }

        return roomAvailability;
    }

    @Override
    public RoomDetailsDto getRoom(String roomId) {
        log.info("calling Room Service for getting room details");

        RoomDetailsDto roomDetails = null;

        // Construct URL
        String roomServiceUri = eurekaClientUtil.getServiceUri(roomServiceID);
        String url = roomServiceUri + roomServiceV1 + "/" + roomId;

        try {
            RoomDetailsDto response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(RoomDetailsDto.class)
                    .block();

            log.info("{} response: {}", roomServiceV1Name, response);

            roomDetails = response;
        }catch (WebClientResponseException webClientResponseException){
            handleWebClientException(webClientResponseException);
        }catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new BookingServiceRuntimeException(ex.getMessage());
        }

        return roomDetails;
    }

    private void handleWebClientException(WebClientResponseException webClientResponseException) {
        log.info("Handling WebClientException");

        // Convert error response to JSONObject
        JSONObject response = new JSONObject(webClientResponseException.getResponseBodyAsString());

        log.info("Error response from {} Service is: {}", roomServiceV1Name, response);

        String errorCode = response.getString("errorCode");
        String message = response.getString("message");
        String details = response.has("details") ? response.getString("details") : "";

        throw new RoomServiceException(errorCode, message, details);
    }
}