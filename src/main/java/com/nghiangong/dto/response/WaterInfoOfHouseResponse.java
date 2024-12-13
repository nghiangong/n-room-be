package com.nghiangong.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaterInfoOfHouseResponse {
    String waterChargeCalc;
    int waterChargePerPerson;
    int waterPricePerUnit;
}
