package com.myroom.onboardingservice.usecase.impl;

import com.myroom.onboardingservice.data.dto.OrganizationRequestDto;
import com.myroom.onboardingservice.data.dto.OrganizationResponseDto;
import com.myroom.onboardingservice.exception.OnboardingServiceRuntimeException;
import com.myroom.onboardingservice.exception.OrganizationServiceException;
import com.myroom.onboardingservice.usecase.OrganizationService;
import com.myroom.onboardingservice.util.EurekaClientUtil;
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
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    WebClient webClient;

    @Autowired
    EurekaClientUtil eurekaClientUtil;

    @Value("${services.organization-service.id}")
    String organizationServiceID;

    @Value("${services.organization-service.v1.api}")
    String organizationServiceV1;

    @Value("${services.organization-service.v1.name}")
    String organizationServiceV1Name;


    @Override
    public OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequestDto) {
        log.info("Calling {} Service for creating organization: {}", organizationServiceV1Name, organizationRequestDto);

        // Construct URL
        String organizationServiceURI = eurekaClientUtil.getServiceUri(organizationServiceID);
        String url = organizationServiceURI + organizationServiceV1 + "/org";

        try{
            OrganizationResponseDto organizationResponseDto = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(organizationRequestDto)
                    .retrieve()
                    .bodyToMono(OrganizationResponseDto.class)
                    .block();

            log.info("Organization created successfully: {}", organizationResponseDto);
            return organizationResponseDto;
        } catch (WebClientResponseException webClientResponseException) {
            handleWebClientException(webClientResponseException);
            return null;
        } catch (Exception ex){
            throw new OnboardingServiceRuntimeException(ex.getMessage());
        }
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