package com.myroom.authserver.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.firebase.auth.AuthErrorCode;
import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"stackTrace","cause","message", "suppressed", "localizedMessage"})
public class AuthServiceFirebaseInvalidTokenException extends RuntimeException{
    public final AuthErrorCode errorCode;
    public final String errorMessage;

    public AuthServiceFirebaseInvalidTokenException(AuthErrorCode errorCode, String errorMessage){
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
