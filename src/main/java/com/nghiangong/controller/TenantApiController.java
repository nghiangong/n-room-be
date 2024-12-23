package com.nghiangong.controller;

import java.util.List;

import com.nghiangong.dto.request.tenant.TenantReq;
import com.nghiangong.dto.response.*;
import com.nghiangong.dto.response.contract.ContractDetailRes;
import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.dto.response.elecwater.RecordRes;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.dto.response.invoice.InvoiceRes;
import com.nghiangong.dto.response.room.RoomDetailRes2;
import com.nghiangong.dto.response.user.UserRes;
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
                .result(tenantApiService.getRoom())
                .build();
    }

    @GetMapping("/contracts")
    ApiResponse<List<ContractRes>> getContracts() {
        return ApiResponse.<List<ContractRes>>builder()
                .result(tenantApiService.getContracts())
                .build();
    }

    @GetMapping("/contracts/{id}")
    ApiResponse<ContractDetailRes> getContractDetailById(@PathVariable int id) {
        return ApiResponse.<ContractDetailRes>builder()
                .result(tenantApiService.getContractDetailById(id))
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
    ApiResponse<List<UserRes>> getMembers() {
        return ApiResponse.<List<UserRes>>builder()
                .result(tenantApiService.getMembers())
                .build();
    }

    @PostMapping("/members")
    ApiResponse createMember(@RequestBody TenantReq request) {
        repTenantApiService.createMember(request);
        return ApiResponse.builder()
                .build();
    }

    @PutMapping("/members/{id}")
    ApiResponse updateMember(@PathVariable Integer id, @RequestBody TenantReq request) {
        repTenantApiService.updateMember(id, request);
        return ApiResponse.builder()
                .build();
    }

    @DeleteMapping("/members/{id}")
    ApiResponse deleteMember(@PathVariable Integer id) {
        repTenantApiService.deleteMember(id);
        return ApiResponse.builder()
                .build();
    }
}
