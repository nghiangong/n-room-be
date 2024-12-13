package com.nghiangong.model;

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
public class RecordPair {
    Record prev;
    Record cur;

    public RecordPair(ElecRecordOfRoom cur, ElecRecordOfRoom prev) {
        this.prev = new Record(prev);
        this.cur = new Record(cur);
    }

    public RecordPair(WaterRecordOfRoom cur, WaterRecordOfRoom prev) {
        this.prev = new Record(prev);
        this.cur = new Record(cur);
    }
}
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
class Record {
    Integer id;
    LocalDate date;
    Integer value;

    public Record(ElecRecordOfRoom elecRecordOfRoom) {
        if (elecRecordOfRoom != null) {
            this.id = elecRecordOfRoom.getId();
            this.date = elecRecordOfRoom.getDate();
            this.value = elecRecordOfRoom.getValue();
        }
    }


    public Record(WaterRecordOfRoom waterRecordOfRoom) {
        if (waterRecordOfRoom != null) {
            this.id = waterRecordOfRoom.getId();
            this.date = waterRecordOfRoom.getDate();
            this.value = waterRecordOfRoom.getValue();
        }
    }
}