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
}
