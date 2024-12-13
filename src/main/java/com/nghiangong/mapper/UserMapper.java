package com.nghiangong.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.dto.request.ManagerCreationRequest;
import com.nghiangong.dto.request.ManagerUpdateRequest;
import com.nghiangong.dto.response.UserResponse;
import com.nghiangong.entity.user.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(ManagerCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, ManagerUpdateRequest request);
}
