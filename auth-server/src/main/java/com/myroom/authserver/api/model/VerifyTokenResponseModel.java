package com.myroom.authserver.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myroom.authserver.api.constants.AuthStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyTokenResponseModel {
    @Schema(description = "Auth status", example = "SUCCESS")
    private AuthStatus status;

    @Schema(description = "Message", example = "Token successfully verified")
    private String message;
}