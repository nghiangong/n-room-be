package com.nghiangong.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.elecwater.ElecRecordOfRoom;

@Repository
public interface ElecRecordOfRoomRepository extends JpaRepository<ElecRecordOfRoom, Integer> {
    //    List<ElecRecordOfRoom> findAllByRoomIdAndDateLessThanEqualOrderByDateDesc(int roomId, LocalDate date);
    Optional<ElecRecordOfRoom> findByRoomIdAndDate(int roomId, LocalDate date);

//    @Query("""
//            SELECT e
//            FROM ElecNumberOfRoom e
//            WHERE e.room.id IN (
//                SELECT c.room.id
//                FROM Contract c
//                WHERE c.repTenant.id = (
//                    SELECT t.repTenant.id
//                    FROM Tenant t
//                    WHERE t.id = :tenantId
//                )
//            )
//            ORDER BY e.date DESC
//            LIMIT 5
//            """)
//    List<ElecRecordOfRoom> findByTenant(@Param("tenantId") int tenantId);

//    ElecRecordOfRoom findTopByRoomIdAndDateBetweenOrderByDateDesc(int roomId, LocalDate startDate, LocalDate endDate);
}
