package com.nghiangong.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.dto.request.ManagerCreationRequest;
import com.nghiangong.dto.response.ManagerResponse;
import com.nghiangong.entity.user.Manager;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ManagerMapper {
    Manager toManager(ManagerCreationRequest request);

    ManagerResponse toManagerResponse(Manager manager);
}
