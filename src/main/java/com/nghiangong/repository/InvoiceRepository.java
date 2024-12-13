package com.nghiangong.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.room.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query("SELECT i FROM Invoice i WHERE i.contract.room.house.id = :houseId")
    List<Invoice> findAllByHouseId(@Param("houseId") int houseId);

    @Query(
            "SELECT i FROM Invoice i WHERE i.contract.id IN " +
            "(SELECT c.id FROM Contract c " +
            "WHERE c.repTenant.id = " +
            "(SELECT t.repTenant.id FROM Tenant t WHERE t.id = :tenantId) " +
            "OR c.repTenant.id = :tenantId)")
    List<Invoice> findByTenantId(int tenantId);

    List<Invoice> findByContractRoomHouseManagerId(int id);
}
