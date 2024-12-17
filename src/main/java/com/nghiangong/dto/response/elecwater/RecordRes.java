package com.nghiangong.dto.response.elecwater;

import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import com.nghiangong.entity.elecwater.WaterRecordOfRoom;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecordRes {
    Integer id;
    LocalDate date;
    Integer value;
}
