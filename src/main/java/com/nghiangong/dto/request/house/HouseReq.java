package com.nghiangong.dto.request.house;

import com.nghiangong.constant.ElecChargeCalc;
import com.nghiangong.constant.WaterChargeCalc;
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
    String status;

    List<OtherFeeRes> otherFees;

    //elecs info
    ElecChargeCalc elecChargeCalc;
    List<Integer> elecCostByPeopleCount;
    Integer elecPricePerUnit;

    //waters info
    WaterChargeCalc waterChargeCalc;
    Integer waterChargePerPerson;
    Integer waterPricePerUnit;
}
