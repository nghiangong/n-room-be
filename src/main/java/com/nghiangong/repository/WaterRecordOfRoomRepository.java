package com.nghiangong.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.elecwater.WaterRecordOfRoom;

@Repository
public interface WaterRecordOfRoomRepository extends JpaRepository<WaterRecordOfRoom, Integer> {
    //    List<WaterRecordOfRoom> findAllByRoomIdAndDateLessThanEqualOrderByDateDesc(int roomId, LocalDate date);
    Optional<WaterRecordOfRoom> findByRoomIdAndDate(int roomId, LocalDate date);

//    @Query("""
//            SELECT w
//            FROM WaterNumberOfRoom w
//            WHERE w.room.id IN (
//                SELECT c.room.id
//                FROM Contract c
//                WHERE c.repTenant.id = (
//                    SELECT t.repTenant.id
//                    FROM Tenant t
//                    WHERE t.id = :tenantId
//                )
//            )
//            ORDER BY w.date DESC
//            LIMIT 5
//            """)
//    List<WaterRecordOfRoom> findByTenant(@Param("tenantId") int tenantId);
}
