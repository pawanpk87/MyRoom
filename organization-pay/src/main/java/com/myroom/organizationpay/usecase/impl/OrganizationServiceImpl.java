package com.myroom.organizationpay.usecase.impl;

import com.myroom.organizationpay.data.dto.OrganizationResponseDto;
import com.myroom.organizationpay.data.dto.VerifyCurrentUserResponseDto;
import com.myroom.organizationpay.exception.OrganizationPayServiceRuntimeException;
import com.myroom.organizationpay.exception.OrganizationServiceException;
import com.myroom.organizationpay.usecase.OrganizationService;
import com.myroom.organizationpay.util.EurekaClientUtil;
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
    public OrganizationResponseDto getOrganization(String organizationId) {
        log.info("Calling {} Service for getting organization data for organizationId: {}", organizationServiceV1Name, organizationId);

        // Construct URL
        String organizationServiceURI = eurekaClientUtil.getServiceUri(organizationServiceID);
        String url = organizationServiceURI + organizationServiceV1 + "/org/" + organizationId;

        try{
            OrganizationResponseDto organizationResponseDto = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(OrganizationResponseDto.class)
                    .block();

            log.info("Fetched organization data successfully: {}", organizationResponseDto);
            return organizationResponseDto;
        } catch (WebClientResponseException webClientResponseException) {
            handleWebClientException(webClientResponseException);
        } catch (Exception ex){
            throw new OrganizationPayServiceRuntimeException(ex.getMessage());
        }

        return  null;
    }

    @Override
    public boolean verifyCurrentUser(String organizationId, String uid) {
        log.info("Calling {} Service for verifying current user has the permissions for organizationId: {}", organizationServiceV1Name, organizationId);

        // Construct URL
        String organizationServiceURI = eurekaClientUtil.getServiceUri(organizationServiceID);
        String url = organizationServiceURI + organizationServiceV1 + "/org/" + organizationId + "/permissions/" + uid;

        try{
            VerifyCurrentUserResponseDto verifyCurrentUserResponse = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(VerifyCurrentUserResponseDto.class)
                    .block();

            if(!verifyCurrentUserResponse.isAllowed()){
                log.error(verifyCurrentUserResponse.getMessage());
                throw new OrganizationPayServiceRuntimeException(verifyCurrentUserResponse.getMessage());
            }

            log.info(verifyCurrentUserResponse.getMessage());

            return true;
        } catch (WebClientResponseException webClientResponseException) {
            handleWebClientException(webClientResponseException);
        } catch (Exception ex){
            throw new OrganizationPayServiceRuntimeException(ex.getMessage());
        }

        return false;
    }

    private void handleWebClientException(WebClientResponseException webClientResponseException) {
        log.info("Handling organization WebClientException");

        // Convert error response to JSONObject
        JSONObject response = new JSONObject(webClientResponseException.getResponseBodyAsString());

        log.info("Error response from {} Service is: {}", organizationServiceV1Name, response);

        String errorCode = response.getString("errorCode");
        String message = response.getString("message");
        String details = response.getString("details");

        throw new OrganizationServiceException(errorCode, message, details);
    }
}