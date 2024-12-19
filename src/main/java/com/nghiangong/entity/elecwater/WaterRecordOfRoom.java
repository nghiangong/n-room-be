package com.nghiangong.entity.elecwater;

import java.time.LocalDate;

import jakarta.persistence.*;

import com.nghiangong.entity.room.Room;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class WaterRecordOfRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    LocalDate date;
    Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    Room room;

    public WaterRecordOfRoom(LocalDate date) {
        this.date = date;
    }
}
