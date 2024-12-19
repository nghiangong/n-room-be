package com.nghiangong.dto.response.house;

import com.nghiangong.dto.response.otherFee.OtherFeeRes;
import com.nghiangong.dto.response.user.UserRes;
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

    boolean havingElecIndex;
    boolean havingWaterIndex;

    List<Integer> elecCostByPeopleCount;
    Integer elecPricePerUnit;

    //waters info
    Integer waterChargePerPerson;
    Integer waterPricePerUnit;
}
