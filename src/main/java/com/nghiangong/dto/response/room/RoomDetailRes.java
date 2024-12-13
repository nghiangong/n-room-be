package com.nghiangong.dto.response.room;

import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.dto.response.house.HouseRes;
import com.nghiangong.dto.response.tenant.TenantRes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDetailRes {
    int id;
    String name;
    int price;
    String status;

    HouseRes house;
    ContractRes currentContract;
    TenantRes repTenant;
}
