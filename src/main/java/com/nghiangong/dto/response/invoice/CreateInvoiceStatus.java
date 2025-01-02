package com.nghiangong.dto.response.invoice;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateInvoiceStatus {
    String roomName;
    String status;
    String message;
}
