package com.nghiangong.dto.response.house;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HouseRes {
    Integer id;
    String name;
    String address;
    String province;
    String district;
    String ward;
    String status;
}
