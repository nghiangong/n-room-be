package com.nghiangong.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.room.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    List<Contract> findByRoomId(int roomId);

    Contract findByRepTenantId(int repTenantId);

    @Query("""
            SELECT c FROM Contract c
            JOIN Tenant t ON c.repTenant.id = t.repTenant.id
            WHERE t.id = :tenantId
            """)
    List<Contract> findByTenantId(@Param("tenantId") int tenantId);

    @Query("""
            SELECT c 
            FROM Contract c
            JOIN c.room r
            JOIN r.house h
            WHERE c.startDate <= :lastOfMonth
              AND (c.endDate IS NULL OR :lastOfMonth <= c.endDate)
              AND h.id = :houseId
            """
    )
    List<Contract> findByHouseIdWithoutInvoiceOfMonth(@Param("houseId") int houseId,
                                                      @Param("lastOfMonth") LocalDate lastOfMonth);
}
