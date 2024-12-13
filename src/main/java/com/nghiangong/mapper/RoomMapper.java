package com.nghiangong.mapper;

import com.nghiangong.dto.request.room.RoomReq;
import com.nghiangong.dto.response.room.RoomDetailRes2;
import com.nghiangong.dto.response.room.RoomNameRes;
import com.nghiangong.dto.response.room.RoomDetailRes;
import com.nghiangong.dto.response.room.RoomRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.entity.room.Room;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoomMapper {
    void updateRoom(@MappingTarget Room room, RoomReq request);

    Room toRoom(RoomReq request);

    RoomRes toRoomRes(Room room);

    @Mapping(source = "currentContract.repTenant", target = "repTenant")
    RoomDetailRes toRoomDetailRes(Room room);

    RoomDetailRes2 toRoomDetailDetailRes2(Room room);

    RoomNameRes toRoomNameRes(Room room);
}
