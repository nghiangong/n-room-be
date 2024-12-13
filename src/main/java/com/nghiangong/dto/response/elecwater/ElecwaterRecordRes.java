package com.nghiangong.dto.response.elecwater;

import com.nghiangong.constant.RoomStatus;
import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import com.nghiangong.entity.elecwater.WaterRecordOfRoom;
import com.nghiangong.entity.room.Room;
import com.nghiangong.model.RecordPair;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElecwaterRecordRes {
    int roomId;
    String roomName;
    RoomStatus status;
    RecordPair elecs;
    RecordPair waters;

    public void setRoom(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getName();
        this.status = room.getStatus();
    }
}

