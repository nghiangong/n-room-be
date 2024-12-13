package com.nghiangong.dto.response.contract;

import com.nghiangong.dto.response.house.HouseRes;
import com.nghiangong.dto.response.room.RoomRes;
import com.nghiangong.dto.response.tenant.TenantRes;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractDetailRes {
    Integer id;
    LocalDate startDate;
    LocalDate endDate;
    Integer rentPrice;
    Integer deposit;
    Integer numberOfPeople;
    Integer numberOfBicycle;
    Integer numberOfMotorbike;
    Integer startElecNumber;
    Integer startWaterNumber = 0;
    String status;


    RoomRes room;
    TenantRes repTenant;
    HouseRes house;
}
