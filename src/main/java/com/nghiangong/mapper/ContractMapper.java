package com.nghiangong.mapper;

import com.nghiangong.dto.request.contract.ContractReq;
import com.nghiangong.dto.response.contract.ContractDetailRes;
import com.nghiangong.dto.response.contract.ContractRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.entity.room.Contract;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContractMapper {
    Contract toContract(ContractReq request);

    ContractRes toContractRes(Contract contract);

    @Mapping(source = "room.house", target = "house")
    ContractDetailRes toContractDetailRes(Contract contract);

    void update(@MappingTarget Contract contract, ContractReq request);
}
