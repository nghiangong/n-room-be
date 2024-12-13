package com.nghiangong.entity.room;

import java.time.LocalDate;
import java.util.List;

import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import jakarta.persistence.*;

import com.nghiangong.constant.ContractStatus;
import com.nghiangong.entity.user.Tenant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    LocalDate startDate;
    LocalDate endDate;
    LocalDate terminationDate;

    Integer rentPrice;
    Integer deposit;
    Integer numberOfPeople;
    Integer numberOfBicycle;
    Integer numberOfMotorbike;
    Integer startElecNumber;
    Integer startWaterNumber;

    @Enumerated(EnumType.STRING)
    ContractStatus status = ContractStatus.ACTIVE;

    @OneToOne(cascade = CascadeType.ALL)
    Tenant repTenant;

    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;

    @OneToMany(mappedBy = "contract")
    List<Invoice> invoices;



    @PrePersist
    public void prePersist() {
        if (terminationDate == null) {
            terminationDate = endDate;
        }
        if (status == null) status = ContractStatus.ACTIVE;
    }

//    @PrePersist
    @PreUpdate
    public void preSave() {
        if (!startDate.isAfter(endDate)) throw new AppException(ErrorCode.CONTRACT_INVALID, "Hợp đồng phải có ngày bắt đầu trước ngày kết thúc!");
    }
}
