package com.nghiangong.dto.request.invoice;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InvoiceOfHouseReq {
    int houseId;
    LocalDate month;
}
