//package com.nghiangong.service;
//
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.*;
//
//import com.nghiangong.entity.elecwater.WaterRecordOfRoom;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import com.nghiangong.dto.request.WaterNumberAddRequest;
//import com.nghiangong.dto.response.WaterNumberOfRoomResponse;
//import com.nghiangong.entity.room.Room;
//import com.nghiangong.mapper.WaterMapper;
//import com.nghiangong.repository.RoomRepository;
//import com.nghiangong.repository.WaterRecordOfRoomRepository;
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
//public class WaterService {
//    RoomRepository roomRepository;
//    WaterRecordOfRoomRepository waterNumberOfRoomRepository;
//    WaterMapper waterMapper;
//
//    public List<WaterNumberOfRoomResponse.Pair> getPairList(int houseId, LocalDate date) {
//        List<Room> rooms = roomRepository.findByHouseIdOrderByNameAsc(houseId);
//        List<WaterNumberOfRoomResponse.Pair> responses = new ArrayList<>();
//        for (Room room : rooms) {
//            var prev = getBeforeDate(room.getId(), date);
//            var cur = getByDate(room.getId(), date);
//            responses.addLast(waterMapper.toPair(room, prev, cur));
//        }
//        return responses;
//    }
//
//    public void addWaterNumber(WaterNumberAddRequest request) {
//        System.out.println(request.toString());
//        List<WaterNumberAddRequest.WaterNumberAdd> list = request.getList();
//        for (var x : list) {
//            Room room = roomRepository.findById(x.getRoomId()).orElseThrow();
//            var newWaterNumber = new WaterRecordOfRoom(0, x.getDate(), x.getValue(), room);
//            waterNumberOfRoomRepository.save(newWaterNumber);
//        }
//    }
//
//    public WaterRecordOfRoom getByDate(int roomId, LocalDate date) {
//        return waterNumberOfRoomRepository.findByRoomIdAndDate(roomId, date);
//    }
//
//    public WaterRecordOfRoom getBeforeDate(int roomId, LocalDate date) {
//        LocalDate startDate = YearMonth.from(date).minusMonths(1).atEndOfMonth();
//        LocalDate endDate = date.minusDays(1);
//        return waterNumberOfRoomRepository.findTopByRoomIdAndDateBetweenOrderByDateDesc(roomId, startDate, endDate);
//    }
//
//    public List<WaterNumberOfRoomResponse> getWaterRecordsByTenant() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        Integer tenantId = Integer.valueOf(authentication.getName());
//        var records = waterNumberOfRoomRepository.findByTenant(tenantId);
//        return records.stream().map(waterMapper::toWaterNumberOfRoomResponse).toList();
//    }
//}
