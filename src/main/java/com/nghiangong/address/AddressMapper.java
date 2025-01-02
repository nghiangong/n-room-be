package com.nghiangong.address;

import com.nghiangong.address.entity.District;
import com.nghiangong.address.entity.Province;
import com.nghiangong.address.entity.Ward;
import com.nghiangong.address.response.DistrictResponse;
import com.nghiangong.address.response.ProvinceResponse;
import com.nghiangong.address.response.WardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    ProvinceResponse toProvinceResponse(Province province);

    DistrictResponse toDistrictResponse(District district);

    WardResponse toWardResponse(Ward ward);

    @Mapping(source = "full_name", target = "fullName")
    Province toProvince(DataRequest.ProvinceRequest request);

    @Mapping(source = "full_name", target = "fullName")
    District toDistrict(DataRequest.DistrictRequest request);

    @Mapping(source = "full_name", target = "fullName")
    Ward toWard(DataRequest.WardRequest request);
}
