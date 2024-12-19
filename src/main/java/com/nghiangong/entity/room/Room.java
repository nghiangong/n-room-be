package com.nghiangong.entity.room;

import java.util.List;

import com.nghiangong.constant.ContractStatus;
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

    @Setter(AccessLevel.NONE)
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
        if (contract == null) return;

        switch (this.status) {
            case AVAILABLE:
                this.currentContract = contract;
                this.status = RoomStatus.OCCUPIED;
                break;
            case INACTIVE:
                throw new AppException(ErrorCode.ROOM_INACTIVE);
            case SOON_AVAILABLE:
            case OCCUPIED:
                throw new AppException(ErrorCode.ROOM_HAVING_CONTRACT);
        }
    }

    public void setAvailable() {
        currentContract = null;
        sync();
    }

    public void sync() {
        if (status == RoomStatus.INACTIVE) return;
        if (currentContract == null) status = RoomStatus.AVAILABLE;
        else
            switch (currentContract.getStatus()) {
                case ACTIVE:
                    status = RoomStatus.OCCUPIED;
                    break;
                case SOON_INACTIVE:
                case PENDING_CHECKOUT:
                    status = RoomStatus.SOON_AVAILABLE;
                    break;
                default:
                    status = RoomStatus.AVAILABLE;
                    currentContract = null;
            }
    }
}
