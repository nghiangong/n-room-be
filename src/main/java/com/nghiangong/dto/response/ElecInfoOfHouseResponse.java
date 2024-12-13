package com.nghiangong.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElecInfoOfHouseResponse {
    String elecChargeCalc;
    List<Integer> elecCostByPeopleCount;
    int elecPricePerUnit;
}
