package com.nghiangong.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nghiangong.constant.HouseStatus;
import com.nghiangong.constant.RoomStatus;
import com.nghiangong.dto.request.house.HouseReq;
import com.nghiangong.dto.request.room.RoomReq;
import com.nghiangong.dto.response.house.HouseNameRes;
import com.nghiangong.dto.response.house.HouseStatisticRes;
import com.nghiangong.dto.response.room.RoomRes;
import com.nghiangong.entity.room.Room;
import com.nghiangong.mapper.RoomMapper;
import com.nghiangong.repository.OtherFeeRepository;
import com.nghiangong.repository.RoomRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nghiangong.dto.response.house.HouseDetailRes;
import com.nghiangong.entity.House;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.HouseMapper;
import com.nghiangong.repository.HouseRepository;
import com.nghiangong.repository.ManagerRepository;

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
public class HouseService {
    HouseRepository houseRepository;
    HouseMapper houseMapper;
    ManagerRepository managerRepository;

    public List<HouseStatisticRes> getList() {
        var manager = getManager();
        return manager.getHouses().stream()
                .map((house) -> {
                            var houseRes = houseMapper.toHouseStatisticRes(house);

                            List<Room> rooms = house.getRooms();

                            Map<RoomStatus, Long> roomStatusCounts = rooms.stream()
                                    .collect(Collectors.groupingBy(
                                            Room::getStatus,  // Lấy trạng thái từ phòng
                                            Collectors.counting()  // Đếm số lượng phòng theo trạng thái
                                    ));

                            houseRes.setRoomsCount(
                                    roomStatusCounts.values().stream().mapToInt(Long::intValue).sum()  // Sum the values
                            );
                            houseRes.setAvailableRoomsCount(roomStatusCounts.getOrDefault(RoomStatus.AVAILABLE, 0L).intValue());
                            houseRes.setOccupiedRoomsCount(roomStatusCounts.getOrDefault(RoomStatus.OCCUPIED, 0L).intValue());
                            houseRes.setInactiveRoomsCount(roomStatusCounts.getOrDefault(RoomStatus.INACTIVE, 0L).intValue());
                            houseRes.setAvailableSoonRoomsCount(
                                    roomStatusCounts.getOrDefault(RoomStatus.SOON_AVAILABLE, 0L).intValue());

                            return houseRes;
                        }
                )
                .toList();
    }

    @Transactional
    public HouseDetailRes getHouse(int id) {
        var manager = getManager();
        return houseMapper.toHouseDetailRes(manager.getHouse(id));
    }

    @Transactional
    public void createHouse(HouseReq request) {
        var manager = getManager();

        House newHouse = houseMapper.toHouse(request);
        manager.createHouse(newHouse);
        if (newHouse.getOtherFees() != null)
            newHouse.getOtherFees().forEach(otherFee ->
                    otherFee.setHouse(newHouse));
    }

    @Transactional
    public void updateHouse(int id, HouseReq request) {
        var manager = getManager();

        var house = manager.getHouse(id);
        houseMapper.updateHouse(house, request);
        house.getOtherFees().forEach(otherFee ->
                otherFee.setHouse(house));
        houseRepository.save(house);
    }

    @Transactional
    public List<HouseNameRes> getNameList() {
        var manager = getManager();

        return manager.getHouses().stream().map(houseMapper::toHouseNameRes).toList();
    }

    @Transactional
    public void active(int id) {
        var manager = getManager();
        var house = manager.getHouse(id);
        house.setActive(true);
    }

    @Transactional
    public void inactive(int id) {
        var manager = getManager();
        var house = manager.getHouse(id);
        for (Room room : house.getRooms())
            if (room.getStatus() == RoomStatus.OCCUPIED ||
                room.getStatus() == RoomStatus.SOON_AVAILABLE)
                throw new AppException(ErrorCode.HOUSE_HAVING_OCCUPIED_ROOM);
        house.setActive(false);
    }

    @Transactional
    public void delete(int id) {
        var manager = getManager();
        var house = manager.getHouse(id);
        manager.deleteHouse(house);
        houseRepository.deleteById(id);
    }

    Manager getManager() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int managerId = Integer.parseInt(authentication.getName());
        return managerRepository.findById(managerId).orElseThrow();
    }
}
