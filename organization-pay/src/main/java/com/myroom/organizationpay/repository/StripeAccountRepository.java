package com.myroom.organizationpay.repository;

import com.myroom.organizationpay.api.constants.stripe.StripeAccountStatus;
import com.myroom.organizationpay.data.entity.StripeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeAccountRepository extends JpaRepository<StripeAccount, String> {
    @Query("select stripeAccount.status " +
            "from StripeAccount stripeAccount "+
            "where stripeAccount.organizationRef=:organizationId")
    StripeAccountStatus findStatusByOrganizationId(@Param("organizationId") String organizationId);

    Optional<StripeAccount> findByAccountId(String accountId);

    @Query("update StripeAccount stripeAccount "+
            "set stripeAccount.status=:status "+
            "where stripeAccount.organizationRef=:organizationId")
    void updateStatusByOrganizationId(@Param("organizationId") String organizationId, @Param("status") StripeAccountStatus stripeAccountStatus);
}