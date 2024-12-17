package com.nghiangong.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.elecwater.ElecRecordOfRoom;

@Repository
public interface ElecRecordOfRoomRepository extends JpaRepository<ElecRecordOfRoom, Integer> {
    Optional<ElecRecordOfRoom> findByRoomIdAndDate(int roomId, LocalDate date);

    @Query("""
        SELECT e FROM ElecRecordOfRoom e
        WHERE e.room.id = (
            SELECT r.id FROM Room r
            JOIN r.currentContract c
            JOIN Tenant t ON c.repTenant.id = t.repTenant.id
            WHERE t.id = :tenantId
        )
        ORDER BY e.date DESC
    """)
    List<ElecRecordOfRoom> findByTenantId(Integer tenantId, Pageable pageable);
}
