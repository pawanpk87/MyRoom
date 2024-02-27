package com.myroom.bookingservice.data.entity;

import com.myroom.bookingservice.api.constants.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.Instant;

@Entity
@Table(name = "booking_requests")
@Data
public class BookingRequestDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom-booking-request-id")
    @GenericGenerator(name = "custom-booking-request-id", strategy = "com.myroom.bookingservice.utils.CustomBookingRequestIdGenerator")
    String id;

    @Column(name = "roomId", nullable = false)
    String roomId;

    @Column(name = "checkIn", nullable = false)
    String checkIn;

    @Column(name = "checkOut", nullable = false)
    String checkOut;

    @Column(name = "guests", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    Guest guests;

    @Column(name = "organizationId", nullable = false)
    String organizationId;

    @Column(name = "uid", nullable = false)
    String uid;

    @Column(name = "contactDetails")
    @JdbcTypeCode(SqlTypes.JSON)
    ContactDetails contactDetails;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    BookingRequestStatus status;

    @Column(name = "paymentType")
    @Enumerated(EnumType.STRING)
    PaymentType paymentType;

    @Column(name = "paymentMethodType")
    @Enumerated(EnumType.STRING)
    PaymentMethodType paymentMethodType;

    @Column(name = "bookingRequestDate", nullable = false)
    private Instant bookingRequestDate;

    @CreatedDate
    @Column(name = "createdAt")
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updatedAt", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected  void prePersist(){
        if(this.createdAt == null){
            createdAt = Instant.now();
        }
        if(this.updatedAt == null){
            updatedAt = Instant.now();
        }
    }

    @PreUpdate
    protected void preUpdate(){
        this.updatedAt = Instant.now();
    }
}