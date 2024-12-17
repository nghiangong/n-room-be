package com.nghiangong.mapper;

import com.nghiangong.dto.response.elecwater.RecordPairRes;
import com.nghiangong.dto.response.elecwater.RecordRes;
import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import com.nghiangong.entity.elecwater.WaterRecordOfRoom;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RecordMapper {
    RecordRes toRecordRes(ElecRecordOfRoom record);
    RecordRes toRecordRes(WaterRecordOfRoom record);

    RecordPairRes toRecordPairRes(ElecRecordOfRoom prev, ElecRecordOfRoom cur);
    RecordPairRes toRecordPairRes(WaterRecordOfRoom prev, WaterRecordOfRoom cur);
}
