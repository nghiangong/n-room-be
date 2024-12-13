package com.nghiangong.entity;

import jakarta.persistence.*;

import com.nghiangong.constant.OtherFeeUnit;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class OtherFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;
    int price;

    @Enumerated(EnumType.STRING)
    OtherFeeUnit unit;

    @ManyToOne
    @JoinColumn(name = "house_id")
    House house;
}
