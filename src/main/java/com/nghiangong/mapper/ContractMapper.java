package com.nghiangong.mapper;

import com.nghiangong.dto.request.contract.ContractReq;
import com.nghiangong.dto.response.contract.ContractDetailRes;
import com.nghiangong.dto.response.contract.ContractRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nghiangong.entity.room.Contract;

@Mapper(componentModel = "spring")
public interface ContractMapper {

    @Mapping(source = "numberOfBicycle", target = "numberOfBicycle", defaultValue = "0")
    @Mapping(source = "numberOfMotorbike", target = "numberOfMotorbike", defaultValue = "0")
    Contract toContract(ContractReq request);

    ContractRes toContractRes(Contract contract);

    @Mapping(source = "room.house", target = "house")
    ContractDetailRes toContractDetailRes(Contract contract);

    @Mapping(source = "numberOfBicycle", target = "numberOfBicycle", defaultValue = "0")
    @Mapping(source = "numberOfMotorbike", target = "numberOfMotorbike", defaultValue = "0")
    @Mapping(target = "repTenant", ignore = true)
    void update(@MappingTarget Contract contract, ContractReq request);
}
