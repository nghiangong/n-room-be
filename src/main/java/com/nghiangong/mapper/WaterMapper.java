//package com.nghiangong.mapper;
//
//import com.nghiangong.entity.elecwater.WaterRecordOfRoom;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//
//import com.nghiangong.dto.response.WaterNumberOfRoomResponse;
//import com.nghiangong.entity.room.Room;
//
//@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface WaterMapper {
//    WaterNumberOfRoomResponse toWaterNumberOfRoomResponse(WaterRecordOfRoom waterNumberOfRoom);
//
//    @Mapping(source = "room.id", target = "roomId")
//    @Mapping(source = "room.name", target = "roomName")
//    @Mapping(source = "prev", target = "prev")
//    @Mapping(source = "cur", target = "cur")
//    WaterNumberOfRoomResponse.Pair toPair(Room room, WaterRecordOfRoom prev, WaterRecordOfRoom cur);
//}
