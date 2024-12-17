package com.nghiangong.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nghiangong.entity.elecwater.WaterRecordOfRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterRecordOfRoomRepository extends JpaRepository<WaterRecordOfRoom, Integer> {
    Optional<WaterRecordOfRoom> findByRoomIdAndDate(int roomId, LocalDate date);

    @Query("""
        SELECT w FROM WaterRecordOfRoom w
        WHERE w.room.id = (
            SELECT r.id FROM Room r
            JOIN r.currentContract c
            JOIN Tenant t ON c.repTenant.id = t.repTenant.id
            WHERE t.id = :tenantId
        )
        ORDER BY w.date DESC
    """)
    List<WaterRecordOfRoom> findByTenantId(Integer tenantId, Pageable pageable);
}
