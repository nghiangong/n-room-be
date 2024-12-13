//package com.nghiangong.mapper;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//
//import com.nghiangong.dto.response.ElecNumberOfRoomResponse;
//import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
//import com.nghiangong.entity.room.Room;
//
//@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//public interface ElecMapper {
//    ElecNumberOfRoomResponse toElecNumberOfRoomResponse(ElecRecordOfRoom elecNumberOfRoom);
//
//    @Mapping(source = "room.id", target = "roomId")
//    @Mapping(source = "room.name", target = "roomName")
//    @Mapping(source = "prev", target = "prev")
//    @Mapping(source = "cur", target = "cur")
//    ElecNumberOfRoomResponse.Pair toPair(Room room, ElecRecordOfRoom prev, ElecRecordOfRoom cur);
//}
