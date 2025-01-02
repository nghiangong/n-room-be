package com.nghiangong.dto.response.invoice;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateInvoicesRes {
    String houseName;
    LocalDate month;
    List<CreateInvoiceStatus> statuses;
}

