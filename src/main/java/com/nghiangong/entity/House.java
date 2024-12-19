package com.nghiangong.entity;

import java.util.List;

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

    @Enumerated(EnumType.STRING)
    HouseStatus status;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    Manager manager;

    boolean havingElecIndex;
    boolean havingWaterIndex;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    List<Room> rooms;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OtherFee> otherFees;

    List<Integer> elecCostByPeopleCount;

    Integer elecPricePerUnit;

    Integer waterPricePerUnit;

    Integer waterChargePerPerson;
}
