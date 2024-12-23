package com.nghiangong.repository;

import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.user.Tenant;

import java.util.List;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Integer> {

//    @Query("""
//        SELECT t FROM Tenant t
//        JOIN Tenant t2 ON t2.repTenant.id = t.repTenant.id
//        WHERE t2.id = :tenantId
//        """)
//    List<Tenant> findMembersByTenantId(@Param("tenantId") int tenantId);

    @Query("""
        SELECT COUNT(t) FROM Tenant t 
        WHERE t.repTenant.id = :repTenantId
    """)
    Integer countMembersByRepTenantId(@Param("repTenantId") int repTenantId);

    List<Tenant> findByRepTenantId(int id);
}
