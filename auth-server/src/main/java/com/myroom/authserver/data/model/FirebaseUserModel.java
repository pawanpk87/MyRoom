package com.myroom.authserver.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseUserModel {
    private String uid;
    private String name;
    private String email;
    private boolean isEmailVerified;
    private String providerId;
    private String picture;
    private boolean disabled;
}
