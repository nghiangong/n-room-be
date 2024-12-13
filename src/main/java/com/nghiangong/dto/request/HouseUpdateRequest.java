package com.nghiangong.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HouseUpdateRequest {
    String name;
    String address;
    String province;
    String district;
    String ward;
    String status;
}
