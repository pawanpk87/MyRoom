package com.myroom.paymentservice.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info());
    }

    @Bean
    public Info info(){
        return new Info()
                .title("Payment Service")
                .description("Payment Service")
                .version("v1")
                .contact(
                        new Contact()
                                .name("Pawan Kumar Mehta")
                                .url("https://github.com/pawanpk87")
                                .email("arowpk@gmail.com")
                )
                .license(new License().name("MyRoom internal use."));
    }
}
