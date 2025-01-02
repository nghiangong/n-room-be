package com.nghiangong.controller;

import com.nghiangong.dto.request.contract.ContractReq;
import com.nghiangong.dto.request.contract.StopContractReq;
import com.nghiangong.dto.request.invoice.CheckoutInvoiceReq;
import com.nghiangong.dto.request.invoice.InvoiceReq;
import com.nghiangong.dto.response.contract.ContractDetailRes;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.service.InvoiceService;
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
    InvoiceService invoiceService;

    @GetMapping
    ApiResponse<List<ContractDetailRes>> getList() {
        return ApiResponse.<List<ContractDetailRes>>builder()
                .result(contractService.getList())
                .build();
    }

    @PostMapping
    ApiResponse createContact(@RequestBody ContractReq request) {
        contractService.createContract(request);
        return ApiResponse.builder()
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse updateContract(@PathVariable int id, @RequestBody ContractReq request) {
        contractService.updateContract(id, request);
        return ApiResponse.builder().build();
    }

    @PutMapping("/{id}/stop")
    ApiResponse stopContract(@PathVariable int id, @RequestBody StopContractReq request) {
        contractService.stopContract(id, request);
        return ApiResponse.builder().build();
    }

    @DeleteMapping("/{id}")
    ApiResponse delete(@PathVariable int id) {
        contractService.delete(id);
        return ApiResponse.builder().build();
    }

    @PutMapping("/{id}/checkoutInvoice")
    ApiResponse<InvoiceDetailRes> createCheckoutInvoice(@PathVariable int id, @RequestBody CheckoutInvoiceReq request) {
        return ApiResponse.<InvoiceDetailRes>builder()
                .result(invoiceService.createCheckoutInvoice(id, request))
                .build();
    }

    @PutMapping("/{id}/invoice")
    ApiResponse<InvoiceDetailRes> createInvoice(@PathVariable int id, @RequestBody InvoiceReq request) {
        return ApiResponse.<InvoiceDetailRes>builder()
                .result(invoiceService.createInvoice(id, request))
                .build();
    }
}
