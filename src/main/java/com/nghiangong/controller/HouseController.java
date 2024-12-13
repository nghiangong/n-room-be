package com.nghiangong.controller;

import java.util.List;

import com.nghiangong.dto.request.house.HouseReq;
import com.nghiangong.dto.request.room.RoomReq;
import com.nghiangong.dto.response.*;
import com.nghiangong.dto.response.house.HouseDetailRes;
import com.nghiangong.dto.response.house.HouseNameRes;
import com.nghiangong.dto.response.house.HouseStatisticRes;
import com.nghiangong.dto.response.room.RoomNameRes;
import com.nghiangong.dto.response.room.RoomRes;
import com.nghiangong.service.RoomService;
import org.springframework.web.bind.annotation.*;

import com.nghiangong.service.HouseService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/houses")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class HouseController {
    HouseService houseService;
    RoomService roomService;

    @GetMapping
    ApiResponse<List<HouseStatisticRes>> getList() {
        return ApiResponse.<List<HouseStatisticRes>>builder()
                .result(houseService.getList())
                .build();
    }

    @PostMapping
    ApiResponse<HouseDetailRes> createHouse(@RequestBody HouseReq request) {
        return ApiResponse.<HouseDetailRes>builder()
                .result(houseService.createHouse(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<HouseDetailRes> getHouse(@PathVariable int id) {
        return ApiResponse.<HouseDetailRes>builder()
                .result(houseService.getHouse(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<HouseDetailRes> updateHouse(@PathVariable int id, @RequestBody HouseReq request) {
        return ApiResponse.<HouseDetailRes>builder()
                .result(houseService.updateHouse(id, request))
                .build();
    }

    @GetMapping("/{id}/roomList")
    ApiResponse<List<RoomRes>> getRoomList(@PathVariable int id) {
        return ApiResponse.<List<RoomRes>>builder()
                .result(roomService.getListByHouseId(id))
                .build();
    }

    @GetMapping("/nameList")
    ApiResponse<List<HouseNameRes>> getNameList() {
        return ApiResponse.<List<HouseNameRes>>builder()
                .result(houseService.getNameList())
                .build();
    }

    @GetMapping("/{id}/roomNameList")
    ApiResponse<List<RoomNameRes>> getNameList(@PathVariable int id) {
        return ApiResponse.<List<RoomNameRes>>builder()
                .result(roomService.getNameList(id))
                .build();
    }
}
