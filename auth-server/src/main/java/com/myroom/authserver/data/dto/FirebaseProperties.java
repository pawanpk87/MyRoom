package com.myroom.authserver.data.dto;

import lombok.Data;

@Data
public class FirebaseProperties {
    int sessionExpiryInDays;
    String databaseUrl;
    boolean enableStrictServerSession;
    boolean enableCheckSessionRevoked;
    boolean enableLogoutEverywhere;
}
