package com.myroom.paymentservice.util;


import java.math.BigDecimal;
import java.security.SecureRandom;

public class Utils {
    public static long calculateXPercentageOfAmount(BigDecimal amount, int percentage) {
        if (amount.longValue() < 0 || percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Invalid input: amount and percentage must be non-negative, and percentage must be between 0 and 100.");
        }

        double result = (amount.longValue() * percentage) / 100.0;

        if (result > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Result exceeds the maximum value of a long.");
        }

        return Math.round(result);
    }

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

    public static Long getSubunitsAmountForINR(Long amount){
        return amount * 100;
    }
}