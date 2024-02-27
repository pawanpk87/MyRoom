package com.myroom.organizationpay.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"stackTrace", "cause", "suppressed", "localizedMessage"})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OrganizationPayWebException extends RuntimeException{
    private final String errorCode;
    private final String message;
    private final String details;

    public OrganizationPayWebException(String errorCode, String message, String details){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }
}