package com.nghiangong.mapper;

import com.nghiangong.dto.request.tenant.TenantReq;
import com.nghiangong.dto.response.user.TenantDetailRes;
import com.nghiangong.dto.response.user.UserRes;
import com.nghiangong.entity.House;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Room;
import com.nghiangong.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.entity.user.Tenant;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TenantMapper {

    UserRes toUserRes(User tenant);

    Tenant toTenant(TenantReq request);
    void updateTenant(@MappingTarget Tenant tenant, TenantReq request);

    @Mapping(source = "tenant.id", target = "id")
    @Mapping(source = "contract", target = "contract")
    @Mapping(source = "room", target = "room")
    @Mapping(source = "house", target = "house")
    TenantDetailRes toTenantDetailRes(Tenant tenant, Contract contract, Room room, House house);


}
