package com.myroom.bookingservice.api.model.query;

import com.myroom.bookingservice.api.constants.QueryRelationalOperator;
import com.myroom.bookingservice.api.constants.SortingType;
import lombok.Data;

import java.util.Date;

@Data
public class AmountRequestModel {
    String amount;
    QueryRelationalOperator operator;
    SortingType sortingType;
}
