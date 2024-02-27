package com.myroom.organizationpay.api.constants;

public final class ApiConstants {
    // API constants
    static final String SCHEME = "https";
    static final String HOSTNAME = "myroom.com";
    static final String VERSION = "v1";


    // API URI
    public final static String ORGANIZATION_PAY_API_V1 = "/api/v1/organizationpay";
    public final static String ORGANIZATION_ACCOUNT_ONBOARDING = "/onboarding";
    public final static String ORGANIZATION_STRIPE_ACCOUNT_EVENTS = "/stripe/events";
    public final static String ORGANIZATION_ACCOUNT = "/account";


    // API error code
    public final static String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public final static String UNAUTHORIZED = "UNAUTHORIZED";
    public final static String BAD_REQUEST = "BAD_REQUEST";
    public final static String NOT_FOUND = "NOT_FOUND";
    public final static String ORGANIZATION_ACCOUNT_NOT_FOUND = "ORGANIZATION_ACCOUNT_NOT_FOUND";
    public final static String ACCOUNT_CREATION_FAILED = "ACCOUNT_CREATION_FAILED";
    public final static String UNAUTHORIZED_USER = "UNAUTHORIZED_USER";


    public final static String MESSAGE_SUCCESS = "Success";
    public final static String MESSAGE_BAD_REQUEST = "Bad Request";
    public final static String MESSAGE_UNAUTHORIZED = "Unauthorized";
    public final static String MESSAGE_INVALID_BOOKING_REQUEST_DATA = "Invalid booking request data";
    public final static String MESSAGE_INTERNAL_SERVER_ERROR = "Internal Server Error";
    public final static String MESSAGE_NOT_FOUND = "Not found";
    public final static String MESSAGE_SERVICE_UNAVAILABLE = "This service is currently unavailable";
}