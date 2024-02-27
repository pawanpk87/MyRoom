package com.myroom.authserver.data.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
@EnableConfigurationProperties
@Data
public class SecurityProperties {
    public CookieProperties cookieProps;
    public FirebaseProperties firebaseProps;
    public boolean allowCredentials;
    public List<String> allowedOrigins;
    public List<String> allowedHeaders;
    public List<String> exposeHeaders;
    public List<String> allowedMethods;
    public List<String> allowedPublishApis;
}
