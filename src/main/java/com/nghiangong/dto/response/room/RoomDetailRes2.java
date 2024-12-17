package com.nghiangong.dto.response.room;

import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.dto.response.elecwater.RecordRes;
import com.nghiangong.dto.response.house.HouseDetailRes;
import com.nghiangong.dto.response.house.HouseRes;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomDetailRes2 {
    int id;
    String name;
    int price;
    String status;

    HouseDetailRes house;
    ContractRes currentContract;

    List<RecordRes> elecRecords;
    List<RecordRes> waterRecords;
}