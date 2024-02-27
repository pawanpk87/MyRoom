package com.myroom.organizationpay.data.entity;

import com.myroom.organizationpay.api.constants.OrganizationAccountStatus;
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
@Table(name = "organization_accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationAccount {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name = "organizationRef", nullable = false, unique = true)
    private String organizationRef;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganizationAccountStatus status;

    @OneToOne(targetEntity = StripeAccount.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "stripeAccountRef", nullable = false)
    private StripeAccount stripeAccountRef;

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