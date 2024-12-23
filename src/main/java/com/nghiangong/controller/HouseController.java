package com.nghiangong.controller;

import java.util.List;

import com.nghiangong.dto.request.house.HouseReq;
import com.nghiangong.dto.response.*;
import com.nghiangong.dto.response.house.HouseDetailRes;
import com.nghiangong.dto.response.house.HouseNameRes;
import com.nghiangong.dto.response.house.HouseStatisticRes;
import com.nghiangong.dto.response.room.RoomNameRes;
import com.nghiangong.dto.response.room.RoomRes;
import com.nghiangong.repository.HouseRepository;
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
    private final HouseRepository houseRepository;
    HouseService houseService;
    RoomService roomService;

    @GetMapping
    ApiResponse<List<HouseStatisticRes>> getList() {
        return ApiResponse.<List<HouseStatisticRes>>builder()
                .result(houseService.getList())
                .build();
    }

    @PostMapping
    ApiResponse createHouse(@RequestBody HouseReq request) {
        houseService.createHouse(request);
        return ApiResponse.builder()
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<HouseDetailRes> getHouse(@PathVariable int id) {
        return ApiResponse.<HouseDetailRes>builder()
                .result(houseService.getHouse(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse updateHouse(@PathVariable int id, @RequestBody HouseReq request) {
        houseService.updateHouse(id, request);
        return ApiResponse.builder()
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

    @PutMapping("/{id}/active")
    ApiResponse active(@PathVariable int id) {
        houseService.active(id);
        return ApiResponse.builder().build();
    }

    @PutMapping("/{id}/inactive")
    ApiResponse inactive(@PathVariable int id) {
        houseService.inactive(id);
        return ApiResponse.builder().build();
    }

    @DeleteMapping("/{id}")
    ApiResponse delete(@PathVariable int id) {
        houseService.delete(id);
        return ApiResponse.builder().build();
    }
}
