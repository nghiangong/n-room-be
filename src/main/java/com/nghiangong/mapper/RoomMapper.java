package com.nghiangong.mapper;

import com.nghiangong.dto.request.room.RoomReq;
import com.nghiangong.dto.response.room.RoomDetailRes2;
import com.nghiangong.dto.response.room.RoomNameRes;
import com.nghiangong.dto.response.room.RoomDetailRes;
import com.nghiangong.dto.response.room.RoomRes;
import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import com.nghiangong.entity.elecwater.WaterRecordOfRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.entity.room.Room;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoomMapper {
    void updateRoom(@MappingTarget Room room, RoomReq request);

    Room toRoom(RoomReq request);

    RoomRes toRoomRes(Room room);

    @Mapping(source = "currentContract.repTenant", target = "repTenant")
    RoomDetailRes toRoomDetailRes(Room room);

    @Mapping(target = "elecRecords", ignore = true)
    @Mapping(target = "waterRecords", ignore = true)
    RoomDetailRes2 toRoomDetailDetailRes2(Room room);

    RoomNameRes toRoomNameRes(Room room);
}
