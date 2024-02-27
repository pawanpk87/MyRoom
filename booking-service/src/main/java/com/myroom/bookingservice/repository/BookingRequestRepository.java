package com.myroom.bookingservice.repository;

import com.myroom.bookingservice.api.constants.BookingRequestStatus;
import com.myroom.bookingservice.data.entity.BookingRequestDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface BookingRequestRepository extends JpaRepository<BookingRequestDetails, String> {
    @Modifying
    @Query("update BookingRequestDetails bookingRequestDetails "+
            "set bookingRequestDetails.status=:status "+
            "where bookingRequestDetails.id=:id")
    void updateStatusById(@Param("status") BookingRequestStatus bookingRequestStatus, @Param("id") String bookingRequestId);

    @Query("select bookingRequestDetails.status "+
            "from BookingRequestDetails bookingRequestDetails "+
            "where bookingRequestDetails.id=:id")
    BookingRequestStatus getStatusById(@Param("id") String bookingRequestId);
}