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
    OtherFeeRepository otherFeeRepository;
    RoomRepository roomRepository;
    HouseRepository houseRepository;
    HouseMapper houseMapper;
    ManagerRepository managerRepository;
    RoomMapper roomMapper;

    public List<HouseStatisticRes> getList() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt(authentication.getName());

        return houseRepository.findByManagerId(id).stream()
                .map((house) -> {
                    var houseRes = houseMapper.toHouseStatisticRes(house);
                    var roomCounts = roomRepository.countRoomsByStatusForHouse(house);
                    Map<RoomStatus, Long> roomStatusCounts = roomCounts.stream()
                            .collect(Collectors.toMap(
                                    obj -> (RoomStatus) obj[0],  // Trạng thái (status)
                                    obj -> (Long) obj[1]    // Số lượng (count)
                            ));

                    // Cập nhật số lượng phòng theo từng trạng thái vào HouseStatisticRes
                    houseRes.setRoomsCount(
                            roomStatusCounts.values().stream().mapToInt(Long::intValue).sum()  // Sum the values
                    );
                    houseRes.setAvailableRoomsCount(roomStatusCounts.getOrDefault(RoomStatus.AVAILABLE, 0L).intValue());
                    houseRes.setOccupiedRoomsCount(roomStatusCounts.getOrDefault(RoomStatus.OCCUPIED, 0L).intValue());
                    houseRes.setInactiveRoomsCount(roomStatusCounts.getOrDefault(RoomStatus.INACTIVE, 0L).intValue());
                    houseRes.setAvailableSoonRoomsCount(roomStatusCounts.getOrDefault(RoomStatus.SOON_AVAILABLE, 0L).intValue());

                    return houseRes;
                })
                .toList();
    }

    public HouseDetailRes getHouse(int id) {
        //        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //        int id = Integer.parseInt(authentication.getName());
        House house = houseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.HOUSE_NOT_EXISTED));
        return houseMapper.toHouseDetailRes(house);
    }

    public HouseDetailRes createHouse(HouseReq request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt(authentication.getName());
        Manager manager =
                managerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        House newHouse = houseMapper.toHouse(request);
        newHouse.setManager(manager);
        newHouse.getOtherFees().forEach(otherFee ->
                otherFee.setHouse(newHouse));
        houseRepository.save(newHouse);
        return houseMapper.toHouseDetailRes(houseRepository.save(newHouse));
    }

    public HouseDetailRes updateHouse(int id, HouseReq request) {
        House house = houseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.HOUSE_NOT_EXISTED));
        houseMapper.updateHouse(house, request);
        house.getOtherFees().forEach(otherFee ->
                otherFee.setHouse(house));
        house.setStatus(HouseStatus.ACTIVE);
        return houseMapper.toHouseDetailRes(houseRepository.save(house));
    }
    
    public List<HouseNameRes> getNameList() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt(authentication.getName());

        return houseRepository.findByManagerId(id).stream().map(houseMapper::toHouseNameRes).toList();
    }

    @Transactional
    public void delete(int id) {
        houseRepository.deleteById(id);
    }
}
