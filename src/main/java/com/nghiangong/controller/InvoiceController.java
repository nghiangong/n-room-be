package com.nghiangong.controller;

import java.time.LocalDate;
import java.util.List;

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

    @PostMapping
    ApiResponse createInvoice(@RequestParam int houseId, @RequestParam LocalDate date) {
        invoiceService.createInvoice(houseId, date);
        return ApiResponse.builder().build();
    }
//
//    @GetMapping
//    ApiResponse<List<InvoiceRes>> getList(@RequestParam int houseId) {
//        return ApiResponse.<List<InvoiceRes>>builder()
//                .result(invoiceService.getList(houseId))
//                .build();
//    }

    @GetMapping
    ApiResponse<List<InvoiceDetailRes>> getList() {
        return ApiResponse.<List<InvoiceDetailRes>>builder()
                .result(invoiceService.getList())
                .build();
    }
}
