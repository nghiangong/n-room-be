package com.nghiangong.repository;

import java.util.Arrays;
import java.util.List;

import com.nghiangong.constant.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.room.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    List<Contract> findByRoomId(int roomId);

    @Query("""
            SELECT c FROM Contract c
            JOIN Tenant t ON c.repTenant.id = t.repTenant.id
            WHERE t.id = :tenantId
            """)
    List<Contract> findByTenantId(@Param("tenantId") int tenantId);

    List<Contract> findByRoomHouseManagerId(int id);

//    @Query("""
//            SELECT c FROM Contract c
//            JOIN Room m ON m.currentContract.id = c.id
//            WHERE c.repTenant.id = :repTenantId
//            """)
//    List<Contract> findByRepTenantIdAndStatuses(@Param("repTenantId") int repTenantId,
//                                                @Param("statuses") List<ContractStatus> statuses);

}
