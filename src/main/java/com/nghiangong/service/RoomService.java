package com.nghiangong.service;

import java.util.List;

import com.nghiangong.dto.request.room.RoomReq;
import com.nghiangong.dto.response.room.RoomDetailRes;
import com.nghiangong.dto.response.room.RoomNameRes;
import com.nghiangong.dto.response.room.RoomRes;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nghiangong.constant.RoomStatus;
import com.nghiangong.entity.House;
import com.nghiangong.entity.room.Room;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.RoomMapper;
import com.nghiangong.repository.HouseRepository;
import com.nghiangong.repository.RoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasRole('MANAGER')")
public class RoomService {
    RoomRepository roomRepository;
    RoomMapper roomMapper;
    HouseRepository houseRepository;

    public List<RoomDetailRes> getList() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt(authentication.getName());

        return roomRepository.findByHouseManagerId(id).stream().map(roomMapper::toRoomDetailRes).toList();
    }

    public RoomDetailRes getRoom(int id) {
        Room room =
                roomRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        return roomMapper.toRoomDetailRes(room);
    }

    public List<RoomRes> getListByHouseId(int houseId) {
        return roomRepository.findByHouseIdOrderByNameAsc(houseId).stream().map(roomMapper::toRoomRes).toList();
    }



    public void createRoom(RoomReq request) {
        House house = houseRepository
                .findById(request.getHouseId())
                .orElseThrow(() -> new AppException(ErrorCode.HOUSE_NOT_EXISTED));
        Room newRoom = roomMapper.toRoom(request);
        newRoom.setHouse(house);
        newRoom.setStatus(RoomStatus.AVAILABLE);
        newRoom = roomRepository.save(newRoom);
    }

    public void updateRoom(int id, RoomReq request) {
        var room = roomRepository.findById(id).orElseThrow();
        roomMapper.updateRoom(room, request);
        if (room.getHouse().getId() != request.getHouseId()) {
            room.setHouse(houseRepository.findById(request.getHouseId()).orElseThrow());
        }
        roomRepository.save(room);
    }

    public List<RoomNameRes> getNameList(int houseId) {
        return roomRepository.findByHouseIdOrderByNameAsc(houseId).stream().map(roomMapper::toRoomNameRes).toList();
    }
}
