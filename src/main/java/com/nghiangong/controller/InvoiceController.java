package com.nghiangong.controller;

import java.time.LocalDate;
import java.util.List;

import com.nghiangong.dto.request.invoice.InvoiceOfHouseReq;
import com.nghiangong.dto.response.invoice.CreateInvoicesRes;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import org.springframework.web.bind.annotation.*;

import com.nghiangong.dto.response.ApiResponse;
import com.nghiangong.service.InvoiceService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/invoices")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor


public class InvoiceController {
    InvoiceService invoiceService;

    @GetMapping
    ApiResponse<List<InvoiceDetailRes>> getList() {
        return ApiResponse.<List<InvoiceDetailRes>>builder()
                .result(invoiceService.getList())
                .build();
    }

    @PostMapping("/house")
    ApiResponse<CreateInvoicesRes> createInvoices(@RequestBody InvoiceOfHouseReq request) {
        return ApiResponse.<CreateInvoicesRes>builder()
                .result(invoiceService.createInvoices(request))
                .build();
    }

    @PutMapping("/{id}/paid")
    ApiResponse setPaid(@PathVariable("id") Integer id) {
        invoiceService.setPaid(id);
        return ApiResponse.builder().build();
    }

    @DeleteMapping("/{id}")
    ApiResponse delete(@PathVariable("id") int id) {
        invoiceService.delete(id);
        return ApiResponse.builder().build();
    }
}
