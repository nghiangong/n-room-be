package com.nghiangong.entity.room;

import java.time.LocalDate;
import java.util.List;

import com.nghiangong.constant.RoomStatus;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.DateUtils;
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
    LocalDate noteDate;

    Integer rentPrice;
    Integer deposit;
    Integer numberOfPeople;
    Integer numberOfBicycle;
    Integer numberOfMotorbike;
    Integer startElecNumber;
    Integer startWaterNumber;

    Integer endElecNumber;
    Integer endWaterNumber;

    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    ContractStatus status = ContractStatus.ACTIVE;

    @OneToOne(cascade = CascadeType.ALL)
    Tenant repTenant;

    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    List<Invoice> invoices;

    public void setRoom(Room room) {
        if (room == null) return;
        if (this.room == room) return;
        switch (this.status) {
            case ACTIVE:
            case SOON_INACTIVE:
            case PENDING_CHECKOUT:
                if (this.room != null)
                    this.room.setStatus(RoomStatus.AVAILABLE);
                this.room = room;
                this.room.setCurrentContract(this);
                break;
        }
    }

    public void sync() {
        LocalDate today = LocalDate.now();
        switch (status) {
            case INACTIVE, PENDING_PAYMENT: return;
            default:
                if (!endDate.isAfter(today))
                    status = ContractStatus.PENDING_CHECKOUT;
                else
                    if (DateUtils.remainingDateLessAMonth(endDate))
                        status = ContractStatus.SOON_INACTIVE;
                    else
                        status = ContractStatus.ACTIVE;
                break;
        }
    }

    public void setEndDate(LocalDate endDate) {
        switch (status) {
            case INACTIVE, PENDING_PAYMENT:
                throw new AppException(ErrorCode.NOT_CHANGE_END_DATE);
            default:
                this.endDate = endDate;
                sync();
        }
    }

    @PreRemove
    public void preRemove() {
        if (this.room.getCurrentContract() == this)
            room.setAvailable();
    }
}
