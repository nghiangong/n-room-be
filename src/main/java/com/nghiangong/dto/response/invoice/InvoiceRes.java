package com.nghiangong.dto.response.invoice;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceRes {
    int id;
    String name;
    LocalDate startDate;
    LocalDate endDate;
    LocalDate createDate;
    int amount;
    String status;

//    List<InvoiceItemRes> invoiceItems;
}
