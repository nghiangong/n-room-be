//package com.nghiangong.service;
//
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.time.temporal.TemporalAdjusters;
//import java.util.*;
//
//import com.nghiangong.dto.response.ElecNumberOfRoomResponse;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import com.nghiangong.dto.request.EnterRecords;
//import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
//import com.nghiangong.entity.room.Room;
//import com.nghiangong.mapper.ElecMapper;
//import com.nghiangong.repository.ElecRecordOfRoomRepository;
//import com.nghiangong.repository.RoomRepository;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Slf4j
//public class ElecService {
//    RoomRepository roomRepository;
//    ElecRecordOfRoomRepository elecNumberOfRoomRepository;
//    ElecMapper elecMapper;
//
//    public List<ElecNumberOfRoomResponse.Pair> getPairList(int houseId, LocalDate date) {
//        date = date.with(TemporalAdjusters.lastDayOfMonth());
//        List<Room> rooms = roomRepository.findByHouseIdOrderByNameAsc(houseId);
//        List<ElecNumberOfRoomResponse.Pair> responses = new ArrayList<>();
//        for (Room room : rooms) {
//            var prev = getBeforeDate(room.getId(), date);
//            var cur = getByDate(room.getId(), date);
//            responses.addLast(elecMapper.toPair(room, prev, cur));
//        }
//        return responses;
//    }
//
//    public void addElecNumber(EnterRecords request) {
//        System.out.println(request.toString());
//        List<EnterRecords.EnterRecord> list = request.getList();
//        for (var x : list) {
//            Room room = roomRepository.findById(x.getRoomId()).orElseThrow();
//            var newElecNumber = new ElecRecordOfRoom(0, x.getDate(), x.getValue(), room);
//            elecNumberOfRoomRepository.save(newElecNumber);
//        }
//    }
//
//    public ElecRecordOfRoom getByDate(int roomId, LocalDate date) {
//        return elecNumberOfRoomRepository.findByRoomIdAndDate(roomId, date).orElse(null);
//    }
//
//    public ElecRecordOfRoom getBeforeDate(int roomId, LocalDate date) {
//        LocalDate startDate = YearMonth.from(date).minusMonths(1).atEndOfMonth();
//        LocalDate endDate = date.minusDays(1);
//        return elecNumberOfRoomRepository.findTopByRoomIdAndDateBetweenOrderByDateDesc(roomId, startDate, endDate);
//    }
//
//    public List<ElecNumberOfRoomResponse> getElecRecordsByTenant() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        Integer tenantId = Integer.valueOf(authentication.getName());
//        var records = elecNumberOfRoomRepository.findByTenant(tenantId);
//        return records.stream().map(elecMapper::toElecNumberOfRoomResponse).toList();
//    }
//}
