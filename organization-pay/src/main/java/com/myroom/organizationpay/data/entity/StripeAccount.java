package com.myroom.organizationpay.data.entity;

import com.myroom.organizationpay.api.constants.Currency;
import com.myroom.organizationpay.api.constants.stripe.StripeAccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.Instant;

@Entity
@Table(name = "stripe_accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StripeAccount {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "accountId", nullable = false, unique = true)
    private String accountId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "organizationRef", nullable = false)
    private String organizationRef;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StripeAccountStatus status;

    @CreatedDate
    @Column(name = "createdAt", nullable = false)
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