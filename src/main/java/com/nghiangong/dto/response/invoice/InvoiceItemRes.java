package com.nghiangong.dto.response.invoice;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceItemRes {
    int id;
    String name;
    String note;
    int unitPrice;
    String unit;
    int amount;
}
