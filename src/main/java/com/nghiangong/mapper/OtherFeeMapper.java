package com.nghiangong.mapper;

import com.nghiangong.dto.response.otherFee.OtherFeeRes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.dto.request.OtherFeeRequest;
import com.nghiangong.dto.response.OtherFeeResponse;
import com.nghiangong.entity.OtherFee;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OtherFeeMapper {
    OtherFee toOtherFee(OtherFeeRequest request);

    OtherFeeRes toOtherFeeRes(OtherFee otherFee);

    void updateOtherFee(@MappingTarget OtherFee otherFee, OtherFeeRequest request);
}
