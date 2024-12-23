package com.nghiangong.service;

import com.nghiangong.dto.request.room.RecordReq;
import com.nghiangong.dto.response.elecwater.ElecwaterRecordRes;
import com.nghiangong.dto.response.elecwater.RecordPairRes;
import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import com.nghiangong.entity.elecwater.WaterRecordOfRoom;
import com.nghiangong.entity.room.Room;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.mapper.RecordMapper;
import com.nghiangong.model.DateUtils;
import com.nghiangong.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasRole('MANAGER')")
public class ElecWaterRecordService {
    private final ElecRecordOfRoomRepository elecRecordOfRoomRepository;
    ManagerRepository managerRepository;
    RoomRepository roomRepository;
    RecordMapper recordMapper;

    @Transactional
    public List<ElecwaterRecordRes> get(int houseId, LocalDate date) {
        var manager = getManager();
        var house = manager.getHouse(houseId);

        List<ElecwaterRecordRes> response = new ArrayList<>();
        List<Room> rooms = roomRepository.findByHouseIdOrderByNameAsc(houseId);
        for (Room room : rooms) {
            ElecwaterRecordRes res = new ElecwaterRecordRes();
            res.setRoom(room);
            if (house.isHavingElecIndex())
                res.setElecs(getElecRecordPair(room, date));
            if (house.isHavingWaterIndex())
                res.setWaters(getWaterRecordPair(room, date));
            response.add(res);
        }
        return response;
    }

    @Transactional
    public RecordPairRes getElecRecordPair(Room room, LocalDate date) {
        LocalDate endOfMonth = DateUtils.endOfMonth(date);
        LocalDate endOfLastMonth = DateUtils.endOfLastMonth(date);
        ElecRecordOfRoom cur = room.getElecRecord(endOfMonth);
        ElecRecordOfRoom prev = room.getElecRecord(endOfLastMonth);
        if (cur == null) cur = new ElecRecordOfRoom(endOfMonth);
        if (prev == null) prev = new ElecRecordOfRoom(endOfLastMonth);
        return recordMapper.toRecordPairRes(prev, cur);
    }

    @Transactional
    public RecordPairRes getWaterRecordPair(Room room, LocalDate date) {
        LocalDate endOfMonth = DateUtils.endOfMonth(date);
        LocalDate endOfLastMonth = DateUtils.endOfLastMonth(date);
        WaterRecordOfRoom cur = room.getWaterRecord(endOfMonth);
        WaterRecordOfRoom prev = room.getWaterRecord(endOfLastMonth);
        if (cur == null) cur = new WaterRecordOfRoom(endOfMonth);
        if (prev == null) prev = new WaterRecordOfRoom(endOfLastMonth);
        return recordMapper.toRecordPairRes(prev, cur);
    }

    @Transactional
    Manager getManager() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int managerId = Integer.parseInt(authentication.getName());
        return managerRepository.findById(managerId).orElseThrow();
    }

    @Transactional
    public void addElecRecord(int roomId, RecordReq request) {
        var manager = getManager();
        var room = manager.getRoom(roomId);
        room.addElecRecord(request.getDate(), request.getValue());
    }

    @Transactional
    public void addWaterRecord(int roomId, RecordReq request) {
        var manager = getManager();
        var room = manager.getRoom(roomId);
        room.addWaterRecord(request.getDate(), request.getValue());
    }
}
