package com.nghiangong.mapper;

import com.nghiangong.dto.request.house.HouseReq;
import com.nghiangong.dto.response.house.HouseNameRes;
import com.nghiangong.dto.response.house.HouseStatisticRes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.dto.response.house.HouseDetailRes;
import com.nghiangong.entity.House;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HouseMapper {
    House toHouse(HouseReq request);

    void updateHouse(@MappingTarget House house, HouseReq request);

    HouseDetailRes toHouseDetailRes(House house);

    HouseStatisticRes toHouseStatisticRes(House house);

    HouseNameRes toHouseNameRes(House house);
}
