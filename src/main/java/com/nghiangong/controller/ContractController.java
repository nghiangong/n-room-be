package com.nghiangong.controller;

import com.nghiangong.dto.request.contract.ContractReq;
import com.nghiangong.dto.response.contract.ContractDetailRes;
import org.springframework.web.bind.annotation.*;

import com.nghiangong.dto.response.ApiResponse;
import com.nghiangong.service.ContractService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/contracts")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ContractController {
    ContractService contractService;

    @GetMapping
    ApiResponse<List<ContractDetailRes>> getList() {
        return ApiResponse.<List<ContractDetailRes>>builder()
                .result(contractService.getList())
                .build();
    }

//    @GetMapping("/{contractId}")
//    ApiResponse<ContractRes> getContact(@PathVariable int contractId) {
//        return ApiResponse.<ContractRes>builder()
//                .result(contractService.getContract(contractId))
//                .build();
//    }

    @PostMapping
    ApiResponse createContact(@RequestBody ContractReq request) {
        contractService.createContract(request);
        return ApiResponse.builder()
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse updateContract(@PathVariable int id, @RequestBody ContractReq request) {
        contractService.updateContract(id, request);
        return ApiResponse.builder()
                .build();
    }


    //    @GetMapping
    //    ApiResponse<List<ContractRes>> getContractsByTenant() {
    //        return ApiResponse.<List<ContractRes>>builder()
    //                .result(contractService.getListByTenant())
    //                .build();
    //    }

    @DeleteMapping("/{id}")
    ApiResponse delete(@PathVariable int id) {
        contractService.delete(id);
        return ApiResponse.builder().build();
    }
}
