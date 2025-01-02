package com.nghiangong.address.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Province {
    @Id
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    String fullName;
}
