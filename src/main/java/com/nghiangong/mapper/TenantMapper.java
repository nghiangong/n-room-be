package com.nghiangong.mapper;

import com.nghiangong.dto.response.tenant.TenantDetailRes;
import com.nghiangong.dto.response.tenant.TenantRes;
import com.nghiangong.entity.House;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.entity.user.Tenant;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TenantMapper {

    TenantRes toTenantRes(Tenant tenant);

    @Mapping(source = "tenant.id", target = "id")
    @Mapping(source = "contract", target = "contract")
    @Mapping(source = "room", target = "room")
    @Mapping(source = "house", target = "house")
    TenantDetailRes toTenantDetailRes(Tenant tenant, Contract contract, Room room, House house);
}
