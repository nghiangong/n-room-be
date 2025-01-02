package com.nghiangong.controller;

import com.nghiangong.dto.request.tenant.TenantReq;
import com.nghiangong.dto.response.ApiResponse;
import com.nghiangong.dto.response.user.TenantDetailRes;
import com.nghiangong.service.TenantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenants")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class TenantController {
    TenantService tenantService;

    @GetMapping
    ApiResponse<List<TenantDetailRes>> getTenants() {
        return ApiResponse.<List<TenantDetailRes>>builder()
                .result(tenantService.getTenants())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse updateTenant(@PathVariable Integer id, @RequestBody TenantReq request) {
        tenantService.updateTenant(id, request);
        return ApiResponse.builder()
                .build();
    }
}
