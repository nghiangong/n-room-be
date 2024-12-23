package com.nghiangong.controller;

import java.util.List;

import com.nghiangong.dto.request.room.RoomReq;
import com.nghiangong.dto.response.room.RoomDetailRes;
import org.springframework.transaction.annotation.Transactional;
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

    @PutMapping("/{id}/active")
    ApiResponse active(@PathVariable int id) {
        roomService.active(id);
        return ApiResponse.builder().build();
    }

    @PutMapping("/{id}/inactive")
    ApiResponse inactive(@PathVariable int id) {
        roomService.inactive(id);
        return ApiResponse.builder().build();
    }

    @DeleteMapping("/{id}")
    ApiResponse delete(@PathVariable int id) {
        roomService.delete(id);
        return ApiResponse.builder().build();
    }
}
