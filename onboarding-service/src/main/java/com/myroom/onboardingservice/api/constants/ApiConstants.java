package com.myroom.onboardingservice.api.constants;

public final class ApiConstants {
    // API constants
    static final String SCHEME = "https";
    static final String HOSTNAME = "myroom.com";
    static final String VERSION = "v1";

    // API URI
    public final static String ONBOARDING_SERVICE = "/api/v1/onboarding";
    public final static String ORGANIZATION_ONBOARDING = "/organization";

    // API error code
    public final static String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public final static String UNAUTHORIZED = "UNAUTHORIZED";
    public final static String BAD_REQUEST = "BAD_REQUEST";
    public final static String NOT_FOUND = "NOT_FOUND";


    public final static String MESSAGE_SUCCESS = "Success";
    public final static String MESSAGE_BAD_REQUEST = "Bad Request";
    public final static String MESSAGE_UNAUTHORIZED = "Unauthorized";
    public final static String MESSAGE_INVALID_BOOKING_REQUEST_DATA = "Invalid booking request data";
    public final static String MESSAGE_INTERNAL_SERVER_ERROR = "Internal Server Error";
    public final static String MESSAGE_NOT_FOUND = "Not found";
    public final static String MESSAGE_SERVICE_UNAVAILABLE = "This service is currently unavailable";
}
