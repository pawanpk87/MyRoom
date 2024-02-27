package com.myroom.myroomgateway.validator;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/auth/api/v1/verifyToken",
            "/api/v1/auth/users"
    );

    public Predicate<ServerHttpRequest> isSecured = serverHttpRequest -> {
        String path = serverHttpRequest.getURI().getPath();

        if (path.startsWith("/api/v1/room-service") || path.startsWith("/api/v1/booking-service") || path.startsWith("/api/v1/review-service")
                && serverHttpRequest.getMethod() == HttpMethod.GET) {
            return false;
        }

        return openApiEndpoints.stream()
                .noneMatch(uri-> serverHttpRequest.getURI().getPath().contains(uri));
    };
}
