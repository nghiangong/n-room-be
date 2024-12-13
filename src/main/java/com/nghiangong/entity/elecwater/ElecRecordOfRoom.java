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
public class ElecRecordOfRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    LocalDate date;
    int value;

    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;
}
