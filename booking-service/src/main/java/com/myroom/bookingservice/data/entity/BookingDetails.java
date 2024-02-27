package com.myroom.bookingservice.data.entity;

import com.myroom.bookingservice.api.constants.*;
import com.myroom.bookingservice.data.model.BookingPaymentMetaDataModel;
import com.myroom.bookingservice.data.model.RoomMetaDataModel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.Instant;

@Entity
@Table(name = "bookings")
@Data
public class BookingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom-booking-id")
    @GenericGenerator(name = "custom-booking-id", strategy = "com.myroom.bookingservice.utils.CustomBookingIdGenerator")
    String id;

    @Column(name = "bookingRequestId", nullable = false)
    private String bookingRequestId;

    @Column(name = "organizationId", nullable = false)
    String organizationId;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "checkIn", nullable = false)
    Instant checkIn;

    @Column(name = "checkOut", nullable = false)
    Instant checkOut;

    @Column(name = "peopleCount", nullable = false)
    Long peopleCount;

    @Column(name = "roomDetails", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    RoomMetaDataModel roomDetails;

    @Column(name = "paymentMetaDataModel")
    @JdbcTypeCode(SqlTypes.JSON)
    BookingPaymentMetaDataModel paymentMetaDataModel;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    BookingStatus status;

    @Column(name = "guests", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    Guest guests;

    @Column(name = "contactDetails")
    @JdbcTypeCode(SqlTypes.JSON)
    ContactDetails contactDetails;

    @Column(name = "paymentType")
    @Enumerated(EnumType.STRING)
    PaymentType paymentType;

    @Column(name = "bookingDate", nullable = false)
    private Instant bookingDate;

    @Column(name = "uid", nullable = false)
    String uid;

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