package com.nghiangong.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.room.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByHouseIdOrderByNameAsc(int houseId);

    List<Room> findByHouseManagerId(int managerId);

//    @Query("""
//            SELECT r FROM Room r
//            JOIN r.currentContract c
//            JOIN Tenant t ON c.repTenant.id = t.repTenant.id
//            WHERE t.id = :tenantId
//            """)
//    Optional<Room> findRentingByTenantId(@Param("tenantId") int tenantId);
}
