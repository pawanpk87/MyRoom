package com.myroom.paymentservice.data.entity;

import com.myroom.paymentservice.api.constants.PaymentMethodType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Data
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom-payment-id")
    @GenericGenerator(name = "custom-payment-id", strategy = "com.myroom.paymentservice.util.CustomPaymentIdGenerator")
    String id;

    @Column(name = "paymentId", nullable = false)
    private String paymentId;

    @Column(name = "organizationId", nullable = false)
    private String organizationId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentMethodType type;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    String currency;

    @Column(name = "bookingId", nullable = false)
    private String bookingId;

    @Column(name = "uid", nullable = false)
    String uid;

    @Column(name = "status", nullable = false)
    String status;

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