package com.nghiangong.entity.room;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.nghiangong.constant.PaymentStatus;
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

    Integer rentPrice;
    Integer deposit;
    Integer numberOfPeople;
    Integer numberOfBicycle;
    Integer numberOfMotorbike;
    Integer startElecNumber;
    Integer startWaterNumber;

    Integer endElecNumber;
    Integer endWaterNumber;

    @OneToOne(cascade = CascadeType.ALL)
    Tenant repTenant;

    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;

    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Invoice> invoices;

    public ContractStatus getStatus() {
        if (endDate == null) return ContractStatus.ACTIVE;
        Long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        if (0 < daysBetween && daysBetween < 30) return ContractStatus.SOON_EXPIRED;
        if (!isHavingCheckoutInvoice()) return ContractStatus.PENDING_CHECKOUT;
        if (endDate.isAfter(LocalDate.now())) return ContractStatus.EXPIRED;
        return ContractStatus.ACTIVE;
    }

    public void setRoom(Room newRoom) {

    }

    public void setEndDate(LocalDate endDate) {
        if (this.endDate == endDate) return;
        switch (getStatus()) {
            case EXPIRED:
                throw new AppException(ErrorCode.EXPIRED_CONTRACT_NOT_CHANGE_END_DATE);
            default:
                this.endDate = endDate;
        }
    }

    public PaymentStatus getPaymentStatus() {
        if (getUnpaidInvoiceCount() > 0) return PaymentStatus.UNPAID;
        return PaymentStatus.PAID;
    }

    boolean isHavingCheckoutInvoice() {
        return invoices.stream()
                .anyMatch(invoice -> invoice.isCheckout());
    }


    public int getUnpaidInvoiceCount() {
        return (int) invoices.stream()
                .filter(invoice -> invoice.getStatus() == PaymentStatus.UNPAID)
                .count();
    }


    @PreRemove
    public void preRemove() {
        if (this.room.getCurrentContract() == this)
            room.setCurrentContract(null);
    }
}
