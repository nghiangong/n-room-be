package com.nghiangong.controller;

import java.util.List;

import com.nghiangong.dto.response.*;
import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.dto.response.invoice.InvoiceRes;
import com.nghiangong.dto.response.room.RoomDetailRes2;
import com.nghiangong.dto.response.tenant.TenantRes;
import com.nghiangong.entity.user.Tenant;
import com.nghiangong.service.*;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/tenant")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TenantApiController {
    TenantApiService tenantApiService;
    RepTenantApiService repTenantApiService;

    @GetMapping("/room")
    ApiResponse<RoomDetailRes2> getRoom() {
        return ApiResponse.<RoomDetailRes2>builder()
                .result(tenantApiService.getRoomByTenant())
                .build();
    }

    @GetMapping("/contracts")
    ApiResponse<List<ContractRes>> getContracts() {
        return ApiResponse.<List<ContractRes>>builder()
                .result(tenantApiService.getContracts())
                .build();
    }

    @GetMapping("/invoices")
    ApiResponse<List<InvoiceRes>> getInvoices() {
        return ApiResponse.<List<InvoiceRes>>builder()
                .result(tenantApiService.getInvoices())
                .build();
    }

    @GetMapping("/invoices/{id}")
    ApiResponse<InvoiceDetailRes> getInvoiceDetailById(@PathVariable int id) {
        return ApiResponse.<InvoiceDetailRes>builder()
                .result(tenantApiService.getInvoiceDetailById(id))
                .build();
    }

    @GetMapping("/members")
    ApiResponse<List<TenantRes>> getMembers() {
        return ApiResponse.<List<TenantRes>>builder()
                .result(tenantApiService.getMembers())
                .build();
    }

    @PostMapping("/members")
    ApiResponse createMember(@RequestBody Tenant tenant) {
        repTenantApiService.createMember(tenant);
        return ApiResponse.<TenantRes>builder()
                .build();
    }
//
//    @GetMapping("/elecRecords")
//    ApiResponse<List<ElecNumberOfRoomResponse>> getElecRecords() {
//        return ApiResponse.<List<ElecNumberOfRoomResponse>>builder()
//                .result(elecService.getElecRecordsByTenant())
//                .build();
//    }
//
//    @GetMapping("/waterRecords")
//    ApiResponse<List<WaterNumberOfRoomResponse>> getWaterRecords() {
//        return ApiResponse.<List<WaterNumberOfRoomResponse>>builder()
//                .result(waterService.getWaterRecordsByTenant())
//                .build();
//    }

}
