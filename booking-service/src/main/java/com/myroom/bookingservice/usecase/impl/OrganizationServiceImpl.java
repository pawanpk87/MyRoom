package com.myroom.bookingservice.usecase.impl;

import com.myroom.bookingservice.data.dto.OrganizationData;
import com.myroom.bookingservice.exception.BookingServiceRuntimeException;
import com.myroom.bookingservice.exception.OrganizationServiceException;
import com.myroom.bookingservice.usecase.OrganizationService;
import com.myroom.bookingservice.utils.EurekaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    EurekaClientUtil eurekaClientUtil;

    @Autowired
    WebClient webClient;

    @Value("${services.organization-service.id}")
    String organizationServiceID;

    @Value("${services.organization-service.v1.api}")
    String organizationServiceV1;

    @Value("${services.organization-service.v1.name}")
    String organizationServiceV1Name;

    @Override
    public OrganizationData getOrganization(String organizationId) {
        log.info("calling Organization Service for fetching organization data");

        OrganizationData data = null;

        // Construct URL
        String organizationServiceUri = eurekaClientUtil.getServiceUri(organizationServiceID);
        String url = organizationServiceUri + organizationServiceV1 + "/org/" + organizationId;

        try{
            log.info("Getting organization data for organizationId: {}", organizationId);

            OrganizationData response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(OrganizationData.class)
                    .block();

            log.info("{} response: {}", organizationServiceV1Name, response);

            data = response;
        }catch (WebClientResponseException webClientResponseException){
            handleWebClientException(webClientResponseException);
        }catch (Exception ex){
            log.error("Some error curred: {}", ex.getMessage());
            throw new BookingServiceRuntimeException(ex.getMessage());
        }

        return data;
    }

    @Override
    public boolean isExists(String organizationId) {
        OrganizationData data = getOrganization(organizationId);
        if(data != null){
            return  true;
        }
        return false;
    }

    private void handleWebClientException(WebClientResponseException webClientResponseException) {
        log.info("Handling WebClientException");

        // Convert error response to JSONObject
        JSONObject response = new JSONObject(webClientResponseException.getResponseBodyAsString());

        log.info("Error response from {} Service is: {}", organizationServiceV1Name, response);

        String errorCode = response.getString("errorCode");
        String message = response.getString("message");
        String details = response.getString("details");

        throw new OrganizationServiceException(errorCode, message, details);
    }
}