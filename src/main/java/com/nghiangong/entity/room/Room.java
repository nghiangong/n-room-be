package com.nghiangong.entity.room;

import java.util.List;

import com.nghiangong.constant.PaymentStatus;
import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
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

    private boolean isActive = true;

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

    public RoomStatus getStatus() {
        if (!isActive) return RoomStatus.INACTIVE;
        if (currentContract == null) return RoomStatus.AVAILABLE;
        switch (currentContract.getStatus()) {
            case ACTIVE: return RoomStatus.OCCUPIED;
            case SOON_EXPIRED:
            case PENDING_CHECKOUT:
                return RoomStatus.SOON_AVAILABLE;
            case EXPIRED:
                return RoomStatus.AVAILABLE;
            default:
                return null;
        }
    }

    public PaymentStatus getPaymentStatus() {
        switch (getStatus()) {
            case OCCUPIED:
            case SOON_AVAILABLE:
                return currentContract.getPaymentStatus();
            case INACTIVE:
            case AVAILABLE:
            default:
                return null;
        }
    }
}
