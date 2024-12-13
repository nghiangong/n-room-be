package com.nghiangong.dto.request.contract;

import com.nghiangong.dto.request.tenant.TenantReq;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractReq {
    Integer roomId;
    LocalDate startDate;
    LocalDate endDate;
    Integer rentPrice;
    Integer deposit;
    Integer numberOfPeople;
    Integer numberOfBicycle;
    Integer numberOfMotorbike;
    Integer startElecNumber;
    Integer startWaterNumber;

    TenantReq repTenant;
}
