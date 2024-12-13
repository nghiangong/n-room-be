package com.nghiangong.dto.response.invoice;

import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.dto.response.house.HouseRes;
import com.nghiangong.dto.response.room.RoomRes;
import com.nghiangong.dto.response.tenant.TenantRes;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDetailRes {
    int id;
    String name;
    LocalDate startDate;
    LocalDate endDate;
    LocalDate createDate;
    int amount;
    String status;

    ContractRes contract;
    HouseRes house;
    RoomRes room;
    TenantRes repTenant;
    List<InvoiceItemRes> invoiceItems;
}
