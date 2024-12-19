package com.nghiangong.dto.request.house;

import com.nghiangong.dto.response.otherFee.OtherFeeRes;
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
public class HouseReq {
    String name;
    String address;
    String province;
    String district;
    String ward;

    List<OtherFeeRes> otherFees;

    boolean havingElecIndex;
    boolean havingWaterIndex;

    List<Integer> elecCostByPeopleCount;
    Integer elecPricePerUnit;

    Integer waterChargePerPerson;
    Integer waterPricePerUnit;
}
