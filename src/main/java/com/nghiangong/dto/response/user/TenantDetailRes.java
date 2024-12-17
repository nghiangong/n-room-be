package com.nghiangong.dto.response.user;

import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.dto.response.house.HouseRes;
import com.nghiangong.dto.response.room.RoomRes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantDetailRes {
    int id;
    String username;
    String personalIdNumber;
    String fullName;
    String phoneNumber;
    String email;

    ContractRes contract;
    RoomRes room;
    HouseRes house;
}
