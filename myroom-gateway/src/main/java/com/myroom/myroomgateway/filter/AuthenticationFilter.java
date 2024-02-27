package com.myroom.myroomgateway.filter;

import com.myroom.myroomgateway.dto.VerifyToken;
import com.myroom.myroomgateway.util.EurekaClientUtil;
import com.myroom.myroomgateway.validator.RouteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private WebClient client;
    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    WebClient webClient;

    @Autowired
    EurekaClientUtil eurekaClientUtil;

    @Value("${services.auth-service.id}")
    String authServiceID;

    @Value("${services.auth-service.v1.api}")
    String authServiceV1;

    @Value("${services.auth-service.v1.name}")
    String authServiceV1Name;

    public AuthenticationFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest()) == false){
                return chain.filter(exchange);
            }
            try{
                final String bearerToken = getBearerTokenFromServerHttpRequest(exchange.getRequest());
                return verifyToken(bearerToken)
                        .flatMap(verifyToken -> {
                            System.out.println("verified....");
                            return chain.filter(exchange);
                        }).onErrorResume(exception -> {
                            ServerHttpResponse httpResponse = exchange.getResponse();
                            if(exception instanceof WebClientResponseException) {
                                WebClientResponseException webClientResponseException = (WebClientResponseException) exception;
                                // get the error json response
                                String errorJson = webClientResponseException.getResponseBodyAsString();
                                log.info("errorJson is: {}", errorJson);
                                exchange.getResponse().setStatusCode(webClientResponseException.getStatusCode());
                                return exchange.getResponse().writeWith(
                                        Mono.just(exchange.getResponse().bufferFactory().wrap(errorJson.getBytes()))
                                );
                            }
                            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                            return  exchange.getResponse().writeWith(
                                    Mono.just(exchange.getResponse().bufferFactory().wrap(exception.getMessage().getBytes()))
                            );
                        });
            }catch (Exception ex){
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return  exchange.getResponse().writeWith(
                        Mono.just(exchange.getResponse().bufferFactory().wrap(ex.getMessage().getBytes()))
                );
            }
        };
    }

    private String getBearerTokenFromServerHttpRequest(ServerHttpRequest request) {
        // check if header contains authorization token in header or not
        if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
            throw new RuntimeException("Missing Authorization header");
        }
        final String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw  new RuntimeException("Missing auth token");
        }
        final String bearerToken = authHeader;
        return bearerToken;
    }

    private Mono<VerifyToken> verifyToken(final String bearerToken){
        log.info("Calling auth-service Service for verifying the token: {}", bearerToken);

        // Construct URL
        String organizationServiceURI = eurekaClientUtil.getServiceUri(authServiceID);
        String url = organizationServiceURI + authServiceV1 + "/verifyToken";

        return client.post()
                .uri(url)
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VerifyToken.class);
    }

    public static class Config{

    }
}
