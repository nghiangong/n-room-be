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

    List<Invoice> findByContractRoomHouseManagerId(int id);

    @Query("""
            SELECT i FROM Invoice i
            JOIN i.contract c 
            JOIN Tenant t ON c.repTenant.id = t.repTenant.id
            WHERE t.id = :tenantId
            ORDER BY i.startDate DESC
            """)
    List<Invoice> findByTenantId(int tenantId);

}
