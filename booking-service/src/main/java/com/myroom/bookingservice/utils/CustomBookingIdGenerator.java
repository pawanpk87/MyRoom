package com.myroom.bookingservice.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomBookingIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        String prefix = "bk_";
        String randomId = Utils.generateRandomId(20);
        return prefix + randomId;
    }
}
