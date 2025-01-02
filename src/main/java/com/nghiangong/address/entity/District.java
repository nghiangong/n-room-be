package com.nghiangong.address.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class District {
    @Id
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    String fullName;

    @ManyToOne
    @JoinColumn(name = "province_id")
    Province province;
}
