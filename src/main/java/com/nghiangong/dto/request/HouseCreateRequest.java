package com.nghiangong.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HouseCreateRequest {
    String name;
    String address;
    String province;
    String district;
    String ward;
}
