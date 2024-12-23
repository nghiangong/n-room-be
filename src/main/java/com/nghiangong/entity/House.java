package com.nghiangong.entity;

import java.util.List;

import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import jakarta.persistence.*;

import com.nghiangong.constant.HouseStatus;
import com.nghiangong.entity.room.Room;
import com.nghiangong.entity.user.Manager;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;
    String address;
    String province;
    String district;
    String ward;
    boolean active;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    Manager manager;

    boolean havingElecIndex;
    boolean havingWaterIndex;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    @OrderBy("name ASC")
    List<Room> rooms;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OtherFee> otherFees;

    List<Integer> elecCostByPeopleCount;

    Integer elecPricePerUnit;

    Integer waterPricePerUnit;

    Integer waterChargePerPerson;

    public HouseStatus getStatus() {
        if (active) return HouseStatus.ACTIVE;
        return HouseStatus.INACTIVE;
    }

    public void createRoom(Room newRoom) {
        rooms.add(newRoom);
        newRoom.setHouse(this);
        newRoom.setActive(true);
    }

    public void deleteRoom(Room room) {
        rooms.remove(room);
    }

    @PreRemove
    private void preRemove() {
        if (rooms != null && rooms.size() > 0)
            throw new AppException(ErrorCode.NOT_DELETE_HOUSE);
    }

}
