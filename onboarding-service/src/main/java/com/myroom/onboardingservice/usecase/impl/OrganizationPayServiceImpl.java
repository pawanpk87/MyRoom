package com.myroom.onboardingservice.usecase.impl;

import com.myroom.onboardingservice.data.dto.AccountOnboardingResponseDto;
import com.myroom.onboardingservice.data.dto.OrganizationAccountOnboardingRequestModelDto;
import com.myroom.onboardingservice.data.dto.OrganizationResponseDto;
import com.myroom.onboardingservice.data.dto.StripeAccountLinkResponseDto;
import com.myroom.onboardingservice.exception.OnboardingServiceRuntimeException;
import com.myroom.onboardingservice.exception.OrganizationServiceException;
import com.myroom.onboardingservice.usecase.OrganizationPayService;
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
public class OrganizationPayServiceImpl implements OrganizationPayService {
    @Autowired
    WebClient webClient;

    @Autowired
    EurekaClientUtil eurekaClientUtil;

    @Value("${services.organization-pay.id}")
    String organizationPayServiceID;

    @Value("${services.organization-pay.v1.api}")
    String organizationPayServiceV1;

    @Value("${services.organization-pay.v1.name}")
    String organizationPayServiceV1Name;


    @Override
    public AccountOnboardingResponseDto onboardOrganizationAccount(String organizationId, OrganizationAccountOnboardingRequestModelDto organizationAccountOnboardingRequestModelDto) {
        log.info("Calling {} Service for onboarding organization account", organizationPayServiceV1Name);

        // Construct URL
        String organizationPayServiceURI = eurekaClientUtil.getServiceUri(organizationPayServiceID);
        String url = organizationPayServiceURI + organizationPayServiceV1 + "/onboarding/" + organizationId;

        try{
            AccountOnboardingResponseDto responseDto = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(organizationAccountOnboardingRequestModelDto)
                    .retrieve()
                    .bodyToMono(AccountOnboardingResponseDto.class)
                    .block();
            log.info("Created organization account: {}", responseDto);
            return responseDto;
        } catch (WebClientResponseException webClientResponseException) {
            handleWebClientException(webClientResponseException);
        } catch (Exception ex){
            throw new OnboardingServiceRuntimeException(ex.getMessage());
        }
        return null;
    }

    private void handleWebClientException(WebClientResponseException webClientResponseException) {
        log.info("Handling organization WebClientException");

        // Convert error response to JSONObject
        JSONObject response = new JSONObject(webClientResponseException.getResponseBodyAsString());

        log.info("Error response from {} Service is: {}", organizationPayServiceV1Name, response);

        String errorCode = response.has("errorCode") ? response.getString("errorCode") : "";
        String message = response.getString("message");
        String details = response.has("details") ? response.getString("details") : "";

        throw new OrganizationServiceException(errorCode, message, details);
    }
}