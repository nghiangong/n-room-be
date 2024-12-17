package com.nghiangong.dto.request.invoice;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckoutInvoiceReq {
    int contractId;
    int endElecNumber;
    int endWaterNumber;
}
