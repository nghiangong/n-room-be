package com.nghiangong.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtherFeeRequest {
    String name;
    Integer price;
    String unit;

    Integer houseId;
}
