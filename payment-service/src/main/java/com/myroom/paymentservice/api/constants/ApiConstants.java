package com.myroom.paymentservice.api.constants;


public final class ApiConstants {
    // API constants
    static final String SCHEME = "https";
    static final String HOSTNAME = "api.myroom.com";
    static final String VERSION = "v1";

    // API URI
    public final static String PAYMENT_SERVICE_API_V1 = "/api/v1/payment-service";
    public final static String PAYMENT_ORDER = "/orders";
    public final static String STRIPE_PAYMENT_EVENTS = "/stripe/events";

    public final static String PAYMENT_DASHBOARD = "/dashboard";
    public final static String PAYMENT_STATISTIC = "/statistics";
    public final static String PAYMENT_RECORDS = "/records";

    // API error code
    public final static String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public final static String ORGANIZATION_ACCOUNT_NOT_ACTIVE = "ORGANIZATION_ACCOUNT_NOT_ACTIVE";
    public final static String ORGANIZATION_ACCOUNT_NOT_FOUND = "ORGANIZATION_ACCOUNT_NOT_FOUND";
    public final static String INVALID_PAYMENT_METHOD_TYPE = "INVALID_PAYMENT_METHOD_TYPE";
    public final static String INVALID_PAYMENT = "INVALID_PAYMENT";
    public final static String INVALID_PRICE = "INVALID_PRICE";


    public final static String MESSAGE_SUCCESS = "Success";
    public final static String MESSAGE_BAD_REQUEST = "Bad Request";
    public final static String MESSAGE_UNAUTHORIZED = "Unauthorized";
    public final static String MESSAGE_NOT_FOUND = "Not Found";
    public final static String MESSAGE_INTERNAL_SERVER_ERROR = "Internal Server Error";
    public final static String MESSAGE_SERVICE_UNAVAILABLE = "This service is currently unavailable";
    public final static String MESSAGE_INVALID_PRICE = "Invalid price";
}
