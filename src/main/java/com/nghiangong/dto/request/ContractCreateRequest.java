package com.nghiangong.dto.request;

import java.time.LocalDate;

import com.nghiangong.entity.user.Tenant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractCreateRequest {
    LocalDate startDate;
    LocalDate endDate;
    int rentPrice;
    int deposit = 0;
    int numberOfPeople = 0;
    int numberOfBicycle = 0;
    int numberOfMotorbike = 0;

    Tenant repTenant;

    int roomId;
}
