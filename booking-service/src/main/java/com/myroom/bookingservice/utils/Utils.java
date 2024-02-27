package com.myroom.bookingservice.utils;

import java.security.SecureRandom;

public class Utils {
    public static String generateRandomId(int length){
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder randomId = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            int randomIndex = random.nextInt(CHARACTERS.length());
            randomId.append(CHARACTERS.charAt(randomIndex));
        }
        return randomId.toString();
    }
}
