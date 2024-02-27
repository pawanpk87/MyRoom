package com.myroom.authserver.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseModel {
    @Schema(description = "uid", example = "zwtWhso1vTeEhPC9iBLZM5KMfdF3")
    private String uid;

    @Schema(description = "User Name", example = "name")
    private String name;

    @Schema(description = "Email", example = "name@gamil.com")
    private String email;

    @Schema(description = "Is email verified", example = "true")
    private boolean isEmailVerified;

    @Schema(description = "Provider Id", example = "google")
    private String providerId;

    @Schema(description = "Picture", example = "picURL")
    private String picture;
}
