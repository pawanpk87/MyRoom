package com.myroom.bookingservice.repository;

import com.myroom.bookingservice.data.entity.BookingDetails;
import com.myroom.bookingservice.data.model.BookingPaymentMetaDataModel;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public interface BookingRepository extends JpaRepository<BookingDetails, String> {
    @Query("select bookingDetails.id "+
            "from BookingDetails bookingDetails "+
            "where bookingDetails.bookingRequestId=:bookingRequestId")
    Optional<String> findBookingIdByBookingRequestId(@Param("bookingRequestId") String bookingRequestId);

    @Modifying
    @Query("update BookingDetails bookingDetails "+
            "set bookingDetails.paymentMetaDataModel=:paymentMetaDataModel "+
            "where bookingDetails.id=:id")
    void updatePaymentMetaDataModelById(@Param("id") String id, @Param("paymentMetaDataModel") BookingPaymentMetaDataModel bookingPaymentMetaDataModel);

    Page<BookingDetails> findAll(Specification<BookingDetails> specification, Pageable paeable);

    @Query("SELECT COUNT(b) FROM BookingDetails b "+
            "WHERE b.organizationId=:organizationId")
    Long getTotalBookingByOrganizationId(@Param("organizationId") String organizationId);

    @Query("SELECT COUNT(b) FROM BookingDetails b WHERE b.organizationId = :organizationId AND DATE(b.checkIn) BETWEEN DATE(:startDate) AND DATE(:endDate)")
    Long countBookingsByOrganizationIdAndCheckInDateRange(@Param("organizationId") String organizationId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COUNT(b) FROM BookingDetails b WHERE b.organizationId = :organizationId AND DATE(b.checkOut) BETWEEN DATE(:startDate) AND DATE(:endDate)")
    Long countBookingsByOrganizationIdAndCheckOutDateRange(@Param("organizationId") String organizationId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COUNT(b) FROM BookingDetails b WHERE b.organizationId = :organizationId AND DATE(b.createdAt) BETWEEN DATE(:startDate) AND DATE(:endDate)")
    Long countBookingsByOrganizationIdAndDateRange(@Param("organizationId") String organizationId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT DATE(b.createdAt), COUNT(b) FROM BookingDetails b WHERE b.organizationId = :organizationId AND DATE(b.createdAt) BETWEEN DATE(:startDate) AND DATE(:endDate) GROUP BY DATE(b.createdAt)")
    List<Object[]> countBookingsByOrganizationIdAndDateRangeGroupByDate(@Param("organizationId") String organizationId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    Page<BookingDetails> findByIdContaining(String idContains, Pageable paeable);

    @Query("SELECT "
            + "SUM(CASE WHEN b.peopleCount = 1 THEN 1 ELSE 0 END) AS single, "
            + "SUM(CASE WHEN b.peopleCount = 2 THEN 1 ELSE 0 END) AS double, "
            + "COUNT(*) - SUM(CASE WHEN b.peopleCount IN (1, 2) THEN 1 ELSE 0 END) AS others "
            + "FROM BookingDetails b "
            + "WHERE b.organizationId = :organizationId")
    Map<String, Long> countUniqueByPeopleCountByOrganizationId(String organizationId);

    @Query("SELECT b FROM BookingDetails b WHERE b.organizationId = :organizationId AND DATE(b.createdAt) BETWEEN DATE(:startDate) AND DATE(:endDate)")
    List<BookingDetails> findByOrganizationIdAndCreatedAtBetween(@Param("organizationId") String organizationId, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
}