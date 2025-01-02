package com.nghiangong.controller;

import com.nghiangong.dto.request.user.ChangePasswordReq;
import com.nghiangong.dto.response.user.UserRes;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.nghiangong.dto.request.ManagerCreationRequest;
import com.nghiangong.dto.request.ManagerUpdateRequest;
import com.nghiangong.dto.response.ApiResponse;
import com.nghiangong.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping("/my-info")
    ApiResponse<UserRes> getMyInfo() {
        return ApiResponse.<UserRes>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping
    ApiResponse<UserRes> updateInfo(@RequestBody ManagerUpdateRequest request) {
        return ApiResponse.<UserRes>builder()
                .result(userService.updateInfo(request))
                .build();
    }

    @PutMapping("/changePassword")
    ApiResponse changePassword(@RequestBody ChangePasswordReq request) {
        userService.changePassword(request);
        return ApiResponse.builder().build();
    }
}
