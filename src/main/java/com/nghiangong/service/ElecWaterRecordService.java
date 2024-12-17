package com.nghiangong.service;

import com.nghiangong.dto.request.EnterRecords;
import com.nghiangong.dto.response.elecwater.ElecwaterRecordRes;
import com.nghiangong.dto.response.elecwater.RecordPairRes;
import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import com.nghiangong.entity.elecwater.WaterRecordOfRoom;
import com.nghiangong.entity.room.Room;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.RecordMapper;
import com.nghiangong.repository.ElecRecordOfRoomRepository;
import com.nghiangong.repository.HouseRepository;
import com.nghiangong.repository.RoomRepository;
import com.nghiangong.repository.WaterRecordOfRoomRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    HouseRepository houseRepository;
    RoomRepository roomRepository;
    ElecRecordOfRoomRepository elecRecordOfRoomRepository;
    WaterRecordOfRoomRepository waterRecordOfRoomRepository;
    RecordMapper recordMapper;

    public List<ElecwaterRecordRes> get(int houseId, LocalDate date) {
        houseRepository.findById(houseId).orElseThrow(
                () -> new AppException(ErrorCode.HOUSE_NOT_EXISTED));

        List<ElecwaterRecordRes> resList = new ArrayList<>();
        List<Room> rooms = roomRepository.findByHouseIdOrderByNameAsc(houseId);
        for (Room room : rooms) {
            ElecwaterRecordRes res = new ElecwaterRecordRes();
            res.setRoom(room);
            res.setElecs(getElecRecordPair(room.getId(), date));
            res.setWaters(getWaterRecordPair(room.getId(), date));
            resList.add(res);
        }
        return resList;
    }

    @Transactional
    public void editElecRecord(int id, Integer value) {
        var record = elecRecordOfRoomRepository.getById(id);
        record.setValue(value);
    }

    @Transactional
    public void editWaterRecord(int id, Integer value) {
        var record = waterRecordOfRoomRepository.getById(id);
        record.setValue(value);
    }

    public void enterElecRecords(EnterRecords request) {
        var date = request.getDate().with(TemporalAdjusters.lastDayOfMonth());
        var list = request.getList();
        for (var x : list) {
            Room room = roomRepository.findById(x.getRoomId()).orElseThrow();
            var newRecord = new ElecRecordOfRoom(0, date, x.getValue(), room);
            elecRecordOfRoomRepository.save(newRecord);
        }
    }

    public void enterWaterRecords(EnterRecords request) {
        var date = request.getDate().with(TemporalAdjusters.lastDayOfMonth());
        var list = request.getList();
        for (var x : list) {
            Room room = roomRepository.findById(x.getRoomId()).orElseThrow();
            var newRecord = new WaterRecordOfRoom(0, date, x.getValue(), room);
            waterRecordOfRoomRepository.save(newRecord);
        }
    }

    public RecordPairRes getElecRecordPair(Integer roomId, LocalDate date) {
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate endOfLastMonth = date.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        ElecRecordOfRoom cur = elecRecordOfRoomRepository.findByRoomIdAndDate(roomId, endOfMonth)
                .orElse(null);
        ElecRecordOfRoom prev = elecRecordOfRoomRepository.findByRoomIdAndDate(roomId, endOfLastMonth)
                .orElse(null);
        return recordMapper.toRecordPairRes(cur, prev);
    }

    public RecordPairRes getWaterRecordPair(Integer roomId, LocalDate date) {
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate endOfLastMonth = date.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        WaterRecordOfRoom cur = waterRecordOfRoomRepository.findByRoomIdAndDate(roomId, endOfMonth)
                .orElse(null);
        WaterRecordOfRoom prev = waterRecordOfRoomRepository.findByRoomIdAndDate(roomId, endOfLastMonth)
                .orElse(null);
        return recordMapper.toRecordPairRes(cur, prev);
    }

    public int getElecRecord(Integer roomId, LocalDate date) {
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        var record = elecRecordOfRoomRepository.findByRoomIdAndDate(roomId, endOfMonth)
                .orElseThrow(() -> new AppException(ErrorCode.ELEC_NUMBER_NOT_ENTERED));
        return record.getValue();
    }

    public int getWaterRecord(Integer roomId, LocalDate date) {
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        var record = waterRecordOfRoomRepository.findByRoomIdAndDate(roomId, endOfMonth)
                .orElseThrow(() -> new AppException(ErrorCode.WATER_NUMBER_NOT_ENTERED));
        return record.getValue();
    }
}
