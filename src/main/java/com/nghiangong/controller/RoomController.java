package com.nghiangong.controller;

import java.util.List;

import com.nghiangong.dto.request.room.RoomReq;
import com.nghiangong.dto.response.room.RoomDetailRes;
import org.springframework.web.bind.annotation.*;

import com.nghiangong.dto.response.ApiResponse;
import com.nghiangong.service.RoomService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/rooms")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class RoomController {
    RoomService roomService;

    @GetMapping
    ApiResponse<List<RoomDetailRes>> getList() {
        return ApiResponse.<List<RoomDetailRes>>builder()
                .result(roomService.getList())
                .build();
    }

//    @GetMapping
//    ApiResponse<List<RoomResponse>> getList(@RequestParam int houseId) {
//        return ApiResponse.<List<RoomResponse>>builder()
//                .result(roomService.getList(houseId))
//                .build();
//    }

    @GetMapping("/{id}")
    ApiResponse<RoomDetailRes> getRoom(@PathVariable int id) {
        return ApiResponse.<RoomDetailRes>builder()
                .result(roomService.getRoom(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse updateRoom(@PathVariable int id, @RequestBody RoomReq request) {
        roomService.updateRoom(id, request);
        return ApiResponse.builder()
                .build();
    }

    @PostMapping
    ApiResponse createRoom(@RequestBody RoomReq request) {
        roomService.createRoom(request);
        return ApiResponse.builder()
                .build();
    }
}
