package com.nghiangong.dto.response.house;

import com.nghiangong.constant.ElecChargeCalc;
import com.nghiangong.constant.WaterChargeCalc;
import com.nghiangong.dto.response.otherFee.OtherFeeRes;
import com.nghiangong.dto.response.user.UserRes;
import com.nghiangong.entity.user.Manager;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HouseDetailRes {
    Integer id;
    String name;
    String address;
    String province;
    String district;
    String ward;
    String status;

    List<OtherFeeRes> otherFees;

    UserRes manager;

    //elecs info
    ElecChargeCalc elecChargeCalc;
    List<Integer> elecCostByPeopleCount;
    Integer elecPricePerUnit;

    //waters info
    WaterChargeCalc waterChargeCalc;
    Integer waterChargePerPerson;
    Integer waterPricePerUnit;
}
