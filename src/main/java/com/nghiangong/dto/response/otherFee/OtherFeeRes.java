package com.nghiangong.dto.response.otherFee;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtherFeeRes {
    int id;
    String name;
    int price;
    String unit;
}
