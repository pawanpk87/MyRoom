package com.myroom.paymentservice.usecase.impl;

import com.myroom.paymentservice.data.dto.UpdateRoomStatusPayload;
import com.myroom.paymentservice.exception.PaymentServiceRuntimeException;
import com.myroom.paymentservice.usecase.RoomService;
import com.myroom.paymentservice.util.EurekaClientUtil;
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
public class RoomServiceImpl implements RoomService {
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
    public void updateRoomStatus(UpdateRoomStatusPayload updateRoomStatusPayload) {
        log.info("calling {} Service for updating room details for roomId: {}", roomServiceV1Name, updateRoomStatusPayload.getId());

        // Construct URL
        String roomServiceUri = eurekaClientUtil.getServiceUri(roomServiceID);
        String url = roomServiceUri + roomServiceV1 + "/" + updateRoomStatusPayload.getId() + "/booking";

        try {
             String response = webClient.patch()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateRoomStatusPayload)
                     .retrieve()
                     .bodyToMono(String.class)
                     .block();

             log.info("response: {}", response);

             log.info("Room update successfully");
        } catch (WebClientResponseException webClientResponseException){
             handleWebClientException(webClientResponseException);
        }
    }

    private void handleWebClientException(WebClientResponseException webClientResponseException) {
        log.info("Handling WebClientException");

        // Convert error response to JSONObject
        JSONObject response = new JSONObject(webClientResponseException.getResponseBodyAsString());

        log.info("Error response from {} Service is: {}", roomServiceV1Name, response);

        String errorCode = response.getString("errorCode");
        String message = response.getString("message");
        String details = response.has("details") ? response.getString("details") : "";

        throw new PaymentServiceRuntimeException(message);
    }
}