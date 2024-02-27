package com.myroom.authserver.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UnauthorizedAccess implements AuthenticationEntryPoint {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Unauthorized request {}", request);
        Map<String, Object> errorObject = new HashMap<>();
        int errorCode = 401;
        errorObject.put("message", "Unauthorized access of protected resources, invalid credentials");
        errorObject.put("error", HttpStatus.UNAUTHORIZED);
        errorObject.put("code", errorCode);
        errorObject.put("timestamp", Instant.now());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorObject));
    }
}
