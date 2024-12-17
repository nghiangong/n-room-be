package com.nghiangong.entity.room;

import java.util.List;

import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import jakarta.persistence.*;

import com.nghiangong.constant.RoomStatus;
import com.nghiangong.entity.House;
import com.nghiangong.entity.elecwater.WaterRecordOfRoom;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;
    int price;

    @Enumerated(EnumType.STRING)
    RoomStatus status = RoomStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "house_id", referencedColumnName = "id")
    House house;

    @OneToOne(cascade = CascadeType.ALL)
    Contract currentContract;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    List<Contract> contractList;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    List<Furniture> furnitureList;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ElecRecordOfRoom> elecRecords;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    List<WaterRecordOfRoom> waterRecords;

    public void setCurrentContract(Contract contract) {
        if (contract == null) {
            this.status = RoomStatus.AVAILABLE;
            this.currentContract = null;
        } else
        if (this.status == RoomStatus.AVAILABLE) {
            this.currentContract = contract;
            this.status = RoomStatus.OCCUPIED;
            this.currentContract.setRoom(this);
        }
        else throw new AppException(ErrorCode.ROOM_HAVING_CONTRACT);
    }
}
