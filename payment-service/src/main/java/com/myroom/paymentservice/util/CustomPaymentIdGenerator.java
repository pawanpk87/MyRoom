package com.myroom.paymentservice.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomPaymentIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        String prefix = "pay_";
        String randomId = Utils.generateRandomId(20);
        return prefix + randomId;
    }
}