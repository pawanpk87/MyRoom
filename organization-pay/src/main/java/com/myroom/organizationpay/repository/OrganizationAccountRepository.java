package com.myroom.organizationpay.repository;

import com.myroom.organizationpay.api.constants.OrganizationAccountStatus;
import com.myroom.organizationpay.data.entity.OrganizationAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface OrganizationAccountRepository extends JpaRepository<OrganizationAccount, String> {
    @Query("select organizationAccount " +
            "from OrganizationAccount organizationAccount "+
            "where organizationAccount.organizationRef=:organizationId")
    Optional<OrganizationAccount> findByOrganizationId(@Param("organizationId") String organizationId);

    @Query("select organizationAccount.status " +
            "from OrganizationAccount organizationAccount "+
            "where organizationAccount.organizationRef=:organizationId")
    OrganizationAccountStatus findStatusByOrganizationId(@Param("organizationId") String organizationId);

    Boolean existsByOrganizationRef(String organizationId);

    @Query("update OrganizationAccount organizationAccount "+
            "set organizationAccount.status=:status "+
            "where organizationAccount.organizationRef=:organizationId")
    void updateStatusByOrganizationId(@Param("organizationId") String organizationId, @Param("status") OrganizationAccountStatus organizationAccountStatus);
}