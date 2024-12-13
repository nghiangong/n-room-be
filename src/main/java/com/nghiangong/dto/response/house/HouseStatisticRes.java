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
public class HouseStatisticRes {
    Integer id;
    String name;
    String address;
    String province;
    String district;
    String ward;
    String status;

    Integer roomsCount;
    Integer availableRoomsCount;
    Integer occupiedRoomsCount;
    Integer inactiveRoomsCount;
    Integer availableSoonRoomsCount;
}
