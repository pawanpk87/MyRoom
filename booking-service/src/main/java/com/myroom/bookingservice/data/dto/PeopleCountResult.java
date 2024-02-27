package com.myroom.bookingservice.data.dto;

import lombok.Data;

@Data
public class PeopleCountResult {
    private Long single;
    private Long doubleCount;
    private Long others;
}
