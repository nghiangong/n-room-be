package com.nghiangong.controller;

import java.util.List;

import com.nghiangong.dto.response.ApiResponse;
import com.nghiangong.dto.response.user.TenantDetailRes;
import com.nghiangong.service.TenantService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/tests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class Test {
    TenantService tenantService;

    @GetMapping
    ApiResponse<List<TenantDetailRes>> test() {

        return ApiResponse.<List<TenantDetailRes>>builder()
                .result(tenantService.getTenantsByManager())
                .build();
    }
}
