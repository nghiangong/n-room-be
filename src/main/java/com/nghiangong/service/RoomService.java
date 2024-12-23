package com.nghiangong.service;

import java.util.List;

import com.nghiangong.constant.RoomStatus;
import com.nghiangong.dto.request.room.RoomReq;
import com.nghiangong.dto.response.room.RoomDetailRes;
import com.nghiangong.dto.response.room.RoomNameRes;
import com.nghiangong.dto.response.room.RoomRes;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.repository.ManagerRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nghiangong.entity.room.Room;
import com.nghiangong.mapper.RoomMapper;
import com.nghiangong.repository.RoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasRole('MANAGER')")
public class RoomService {
    ManagerRepository managerRepository;
    RoomRepository roomRepository;
    RoomMapper roomMapper;

    @Transactional
    public List<RoomDetailRes> getList() {
        var manager = getManager();
        var rooms = manager.getRooms();

        return rooms.stream().map(roomMapper::toRoomDetailRes).toList();
    }

    @Transactional
    public RoomDetailRes getRoom(int id) {
        var manager = getManager();
        var room = manager.getRoom(id);
        return roomMapper.toRoomDetailRes(room);
    }

    @Transactional
    public List<RoomRes> getListByHouseId(int houseId) {
        var manager = getManager();
        var house = manager.getHouse(houseId);
        return house.getRooms().stream().map(roomMapper::toRoomRes).toList();
    }

    @Transactional
    public void createRoom(RoomReq request) {
        var manager = getManager();
        var house = manager.getHouse(request.getHouseId());

        Room newRoom = roomMapper.toRoom(request);
        house.createRoom(newRoom);
    }

    @Transactional
    public void updateRoom(int id, RoomReq request) {
        var manager = getManager();
        var room = manager.getRoom(id);
        var house = manager.getHouse(request.getHouseId());

        roomMapper.updateRoom(room, request);
        room.setHouse(house);
    }

    @Transactional
    public List<RoomNameRes> getNameList(int houseId) {
        var manager = getManager();
        var house = manager.getHouse(houseId);
        return house.getRooms().stream().map(roomMapper::toRoomNameRes).toList();
    }

    @Transactional
    public void active(int id) {
        var manager = getManager();
        var room = manager.getRoom(id);
        room.setActive(true);
    }

    @Transactional
    public void inactive(int id) {
        var manager = getManager();
        var room = manager.getRoom(id);
        if (room.getStatus() == RoomStatus.OCCUPIED ||
            room.getStatus() == RoomStatus.SOON_AVAILABLE)
            throw new AppException(ErrorCode.HOUSE_HAVING_OCCUPIED_ROOM);
        room.setActive(false);
    }

    @Transactional
    public void delete(int id) {
        var manager = getManager();
        var room = manager.getRoom(id);
        var house = room.getHouse();
        house.deleteRoom(room);
        roomRepository.deleteById(id);
    }

    Manager getManager() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int managerId = Integer.parseInt(authentication.getName());
        return managerRepository.findById(managerId).orElseThrow();
    }
}
