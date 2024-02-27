package com.myroom.paymentservice.controller.dashboard;

import com.myroom.paymentservice.api.model.dashboard.PaymentStatisticsResponseModel;
import com.myroom.paymentservice.api.model.dashboard.RevenueStatisticsResponseModel;
import com.myroom.paymentservice.api.resource.dashboard.PaymentStatisticDashboardResource;
import com.myroom.paymentservice.usecase.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class PaymentStatisticDashboardController implements PaymentStatisticDashboardResource {
    @Autowired
    PaymentService paymentService;


    @Override
    public ResponseEntity<PaymentStatisticsResponseModel> handleGetBookingStatistics(String organizationId, String startDate, String endDate) {
        log.info("Fetching payment statistics for organizationId: {}", organizationId);
        PaymentStatisticsResponseModel paymentStatisticsResponseModel = paymentService.getBookingStatistics(organizationId, startDate, endDate);
        log.info("Fetched the payment statistics: {}", paymentStatisticsResponseModel);
        return new ResponseEntity<>(paymentStatisticsResponseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RevenueStatisticsResponseModel> handleGetRevenueStatistics(String duration, String organizationId) {
        log.info("Fetching revenue statistics for organizationId: {}", organizationId);
        RevenueStatisticsResponseModel revenueStatisticsResponseModel = paymentService.getRevenueStatistics(organizationId, duration);
        log.info("Fetched the revenue statistics: {}", revenueStatisticsResponseModel);
        return new ResponseEntity<>(revenueStatisticsResponseModel, HttpStatus.OK);
    }
}
