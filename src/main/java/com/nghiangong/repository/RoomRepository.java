package com.nghiangong.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nghiangong.dto.response.room.RoomDetailRes2;
import com.nghiangong.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.room.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByHouseIdOrderByNameAsc(int houseId);

    List<Room> findByHouseManagerId(int managerId);

    @Query("""
            SELECT r FROM Room r
            JOIN r.currentContract c
            JOIN Tenant t ON c.repTenant.id = t.repTenant.id
            WHERE t.id = :tenantId
            """)
    Optional<Room> findRentingByTenantId(@Param("tenantId") int tenantId);
}
