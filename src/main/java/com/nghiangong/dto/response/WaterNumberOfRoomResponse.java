package com.nghiangong.dto.response;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaterNumberOfRoomResponse {
    int id;
    LocalDate date;
    int value;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Pair {
        int roomId;
        int roomName;
        WaterNumberOfRoomResponse prev;
        WaterNumberOfRoomResponse cur;
    }
}
