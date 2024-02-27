package com.myroom.paymentservice.usecase.impl;

import com.myroom.paymentservice.api.constants.AppConstants;
import com.myroom.paymentservice.api.constants.PaymentMethodType;
import com.myroom.paymentservice.api.model.*;
import com.myroom.paymentservice.api.model.dashboard.PaymentStatisticsResponseModel;
import com.myroom.paymentservice.api.model.dashboard.RevenueStatisticsResponseModel;
import com.myroom.paymentservice.data.dto.StripePaymentOrderRequestModel;
import com.myroom.paymentservice.data.dto.StripePaymentOrderResponseModel;
import com.myroom.paymentservice.data.dto.StripePaymentOrderSuccessRequest;
import com.myroom.paymentservice.data.dto.StripePaymentOrderSuccessResponse;
import com.myroom.paymentservice.data.entity.PaymentDetails;
import com.myroom.paymentservice.data.mapper.PaymentMapper;
import com.myroom.paymentservice.exception.InvalidPaymentMethodTypeException;
import com.myroom.paymentservice.exception.PaymentNotFoundException;
import com.myroom.paymentservice.exception.PaymentServiceRuntimeException;
import com.myroom.paymentservice.repository.PaymentDetailsRepository;
import com.myroom.paymentservice.usecase.PaymentService;
import com.myroom.paymentservice.usecase.StripePaymentService;
import com.myroom.paymentservice.util.DateRange;
import com.myroom.paymentservice.util.DateRangeCalculator;
import com.myroom.paymentservice.util.EurekaClientUtil;
import com.myroom.paymentservice.util.Utils;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    StripePaymentService stripePaymentService;

    @Autowired
    PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    EurekaClientUtil eurekaClientUtil;

    @Value("${services.booking-service.id}")
    String bookingServiceID;

    @Value("${services.booking-service.v1.api}")
    String bookingServiceV1;

    @Value("${myRoom.platformFeePercentage}")
    private String myRoomPlatformFeePercentage;


    @Override
    public PaymentOrderResponseModel createOrder(PaymentOrderRequestModel paymentOrderRequestModel) {
        if(paymentOrderRequestModel.getPaymentMethodType() == PaymentMethodType.STRIPE){
            log.info("Redirecting the payment order creation to {} due to payment method type: {}", paymentOrderRequestModel.getPaymentMethodType(), paymentOrderRequestModel.getPaymentMethodType());
            StripePaymentOrderRequestModel stripePaymentOrderRequestModel = paymentMapper.toStripePaymentOrderRequestModel(paymentOrderRequestModel);
            StripePaymentOrderResponseModel stripePaymentOrderResponseModel = stripePaymentService.createOrder(stripePaymentOrderRequestModel);
            PaymentOrderResponseModel paymentOrderResponseModel = paymentMapper.toPaymentOrderResponseModel(stripePaymentOrderResponseModel);
            log.info("Payment Order created: {}", stripePaymentOrderResponseModel);
            return paymentOrderResponseModel;
        }
        else{
            log.error("Invalid payment method type");
            throw new InvalidPaymentMethodTypeException(AppConstants.INVALID_PAYMENT_METHOD_TYPE);
        }
    }

    @Override
    public PaymentOrderSuccessResponse handlePaymentOrderSuccess(PaymentOrderSuccessRequest paymentOrderSuccessRequest) {
        if(paymentOrderSuccessRequest.getPaymentMethodType() == PaymentMethodType.STRIPE){
            log.info("Redirecting to {} for payment order success verification, as the payment method is: {}", paymentOrderSuccessRequest.getPaymentMethodType(), paymentOrderSuccessRequest.getPaymentMethodType());
            StripePaymentOrderSuccessRequest stripePaymentOrderSuccessRequest = paymentMapper.toRazorpayPaymentOrderSuccessRequest(paymentOrderSuccessRequest);
            StripePaymentOrderSuccessResponse stripePaymentOrderSuccessResponse = stripePaymentService.handlePaymentOrderSuccess(stripePaymentOrderSuccessRequest);
            PaymentOrderSuccessResponse paymentOrderSuccessResponse = paymentMapper.toPaymentOrderSuccessResponse(stripePaymentOrderSuccessResponse);
            log.info("payment order successfully verified: {}", paymentOrderSuccessResponse);
            return paymentOrderSuccessResponse;
        }
        else{
            log.error("Invalid payment method type");
            throw new InvalidPaymentMethodTypeException(AppConstants.INVALID_PAYMENT_METHOD_TYPE);
        }
    }

    @Override
    public Long getApplicationFeeAmount(BigDecimal amount) {
        return calculateMyRoomApplicationFeeAmount(amount);
    }

    private Long calculateMyRoomApplicationFeeAmount(BigDecimal amount) {
        Integer percentage = Integer.valueOf(myRoomPlatformFeePercentage);
        return Utils.calculateXPercentageOfAmount(amount, percentage);
    }

    @Override
    public String getSuccessUrl(String bookingId){
        log.info("Creating success url");
        String bookingServiceURI = eurekaClientUtil.getServiceUri(bookingServiceID);
        String url = bookingServiceURI + bookingServiceV1 + "/"+ bookingId + "/payment/success";
        log.info("Created success url: {}", url);
        return url;
    }

    @Override
    public String getCancelUrl(String bookingId){
        log.info("Creating cancel url");
        String bookingServiceURI = eurekaClientUtil.getServiceUri(bookingServiceID);
        String url = bookingServiceURI + bookingServiceV1 + "/"+ bookingId + "/payment/cancel";
        log.info("Created cancel url:{}", url);
        return url;
    }

    @Override
    public PaymentDetailsResponseModel getPaymentDetailsByBookingId(String bookingId) {
        log.info("Fetching the Payment details for bookingId: {}", bookingId);
        Optional<PaymentDetails> paymentDetails = paymentDetailsRepository.findByBookingId(bookingId);
        if(paymentDetails.isEmpty()){
            log.error("Could not find any Payment for bookingId: {}", bookingId);
            throw new PaymentNotFoundException(AppConstants.NOT_FOUND, "Could not find any Payment for bookingId: "+bookingId, null);
        }
        log.info("Fetched Payment:{}", paymentDetails.get());
        return paymentMapper.toPaymentDetailsResponseModel(paymentDetails.get());
    }

    @Override
    public PaymentStatisticsResponseModel getBookingStatistics(String organizationId, String startDate, String endDate) {
        log.info("Fetching the payment statistics");
        BigDecimal total = paymentDetailsRepository.sumAmountByOrganizationIdAndDateRange(organizationId, Instant.parse(startDate), Instant.parse(endDate).plus(1, ChronoUnit.DAYS));
        List<Object[]> data = paymentDetailsRepository.sumAmountByOrganizationIdAndDateRangeGroupByDate(organizationId, Instant.parse(startDate), Instant.parse(endDate).plus(1, ChronoUnit.DAYS));
        BigDecimal totalRevenue = paymentDetailsRepository.getTotalAmountByOrganizationId(organizationId);

        Map<LocalDate, Long> resultMap = new HashMap<>();
        for (Object[] row : data) {
            LocalDate date = ((Date) row[0]).toLocalDate();
            Long count = ((BigDecimal) row[1]).longValue();
            resultMap.put(date, count);
        }

        LocalDate currentDate = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
        List<Long> counts = new ArrayList<>();
        while (!currentDate.isAfter(Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDate())) {
            counts.add(resultMap.getOrDefault(currentDate, 0L));
            currentDate = currentDate.plusDays(1);
        }

        PaymentStatisticsResponseModel paymentStatisticsResponseModel = PaymentStatisticsResponseModel.builder()
                .totalRevenue(totalRevenue)
                .total(total)
                .data(counts)
                .build();

        return paymentStatisticsResponseModel;
    }

    @Override
    public RevenueStatisticsResponseModel getRevenueStatistics(String organizationId, String duration) {
        log.info("Fetching revenue statistics");

        RevenueStatisticsResponseModel revenueStatisticsResponseModel = new RevenueStatisticsResponseModel();
        DateRange dateRange = null;

        switch (duration){
            case "week":
                dateRange = DateRangeCalculator.getDateRange("week");

                List<Object[]> currWeekDatad = paymentDetailsRepository.sumAmountByOrganizationIdAndDateRangeGroupByDate(organizationId, dateRange.getStartDate(), dateRange.getEndDate().plus(1, ChronoUnit.DAYS));
                List<Object[]> prevWeekData = paymentDetailsRepository.sumAmountByOrganizationIdAndDateRangeGroupByDate(organizationId, dateRange.getPreviousStartDate(), dateRange.getPreviousEndDate().plus(1, ChronoUnit.DAYS));

                Map<LocalDate, Long> resultMap1 = new HashMap<>();
                Map<LocalDate, Long> resultMap2 = new HashMap<>();
                for (Object[] row : currWeekDatad) {
                    LocalDate date = ((Date) row[0]).toLocalDate();
                    Long count = ((BigDecimal) row[1]).longValue();
                    resultMap1.put(date, count);
                }
                for (Object[] row : prevWeekData) {
                    LocalDate date = ((Date) row[0]).toLocalDate();
                    Long count = ((BigDecimal) row[1]).longValue();
                    resultMap2.put(date, count);
                }

                LocalDate currentDate = dateRange.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate();
                List<Long> counts1 = new ArrayList<>();
                while (!currentDate.isAfter(dateRange.getEndDate().atZone(ZoneId.systemDefault()).toLocalDate())) {
                    counts1.add(resultMap1.getOrDefault(currentDate, 0L));
                    currentDate = currentDate.plusDays(1);
                }

                currentDate = dateRange.getPreviousStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
                List<Long> counts2 = new ArrayList<>();
                while (!currentDate.isAfter(dateRange.getPreviousEndDate().atZone(ZoneId.systemDefault()).toLocalDate())) {
                    counts2.add(resultMap2.getOrDefault(currentDate, 0L));
                    currentDate = currentDate.plusDays(1);
                }

                revenueStatisticsResponseModel.setCurr(counts1);
                revenueStatisticsResponseModel.setPrev(counts2);
                break;

            default:
                throw new PaymentServiceRuntimeException("Invalid duration type");
        }

        return revenueStatisticsResponseModel;
    }
}