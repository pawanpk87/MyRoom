package com.myroom.authserver.controller;

import com.myroom.authserver.api.model.VerifyTokenResponseModel;
import com.myroom.authserver.api.resource.AuthResource;
import com.myroom.authserver.usecase.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class AuthController implements AuthResource {
    @Autowired
    AuthService authService;

    @Override
    public ResponseEntity<VerifyTokenResponseModel> verifyToken(HttpServletRequest httpServletRequest) {
        log.info("Received verify token request for {}", httpServletRequest);
        VerifyTokenResponseModel verifyTokenResponse = authService.verifyToken(httpServletRequest);
        log.info("verified the token {}", verifyTokenResponse);
        return new  ResponseEntity<>(verifyTokenResponse, HttpStatus.OK);
    }
}