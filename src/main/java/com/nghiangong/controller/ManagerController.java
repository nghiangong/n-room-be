package com.nghiangong.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nghiangong.dto.request.ManagerCreationRequest;
import com.nghiangong.dto.response.ApiResponse;
import com.nghiangong.dto.response.ManagerResponse;
import com.nghiangong.service.ManagerService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/managers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ManagerController {
    ManagerService managerService;

    @PostMapping
    ApiResponse<ManagerResponse> createManager(@RequestBody @Valid ManagerCreationRequest request) {
        return ApiResponse.<ManagerResponse>builder()
                .result(managerService.createManager(request))
                .build();
    }
}
