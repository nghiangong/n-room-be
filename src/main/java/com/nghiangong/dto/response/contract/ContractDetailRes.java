package com.nghiangong.dto.response.contract;

import com.nghiangong.dto.response.house.HouseRes;
import com.nghiangong.dto.response.room.RoomRes;
import com.nghiangong.dto.response.user.UserRes;
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
    Integer startWaterNumber;
    Integer endElecNumber;
    Integer endWaterNumber;
    String status;
    String paymentStatus;

    RoomRes room;
    UserRes repTenant;
    HouseRes house;
}
