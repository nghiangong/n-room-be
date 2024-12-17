package com.nghiangong.dto.response.elecwater;

import com.nghiangong.constant.RoomStatus;
import com.nghiangong.entity.room.Room;
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
    RecordPairRes elecs;
    RecordPairRes waters;

    public void setRoom(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getName();
        this.status = room.getStatus();
    }
}

