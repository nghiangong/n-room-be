package com.nghiangong.entity.room;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.nghiangong.constant.PaymentStatus;
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
@ToString(exclude = {"invoices", "room"})
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Tenant repTenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    Room room;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Invoice> invoices;

    @Transient
    ContractStatus status;

    @Transient
    boolean havingCheckoutInvoice;

    @Transient
    int unpaidInvoiceCount;

    @Transient
    PaymentStatus paymentStatus;

    public boolean isHavingInvoice(LocalDate month) {
        return this.getInvoices().stream().filter(invoice -> DateUtils.isSameMonth(month, invoice.getStartDate()))
                .findFirst().isPresent();
    }

    public Invoice createInvoice(LocalDate month) {
        if (!this.contain(month))
            throw new AppException(ErrorCode.CONTRACT_NOT_CONTAIN_MONTH);
        if (isHavingInvoice(month))
            throw new AppException(ErrorCode.INVOICE_EXISTED);
        var invoice = new Invoice(this, month);
        invoices.add(invoice);
        return invoice;
    }

    public Invoice createCheckoutInvoice() {
        if (LocalDate.now().isBefore(endDate))
            throw new AppException(ErrorCode.CONTRACT_NOT_EXPIRED);
        return createInvoice(endDate);
    }

    @PostLoad
    public void initializeTransients() {
        unpaidInvoiceCount = initializeUnpaidInvoiceCount();
        havingCheckoutInvoice = initializeHavingCheckoutInvoice();
        status = initializeStatus();
        paymentStatus = initializePaymentStatus();
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        if (endDate != null) {
            if (!startDate.isBefore(endDate))
                throw new AppException(ErrorCode.CONTRACT_START_DATE_LESS_THAN_END_DATE);
            if (LocalDate.now().isBefore(endDate)) {
                endElecNumber = null;
                endWaterNumber = null;
            }
        }

        if (room.getHouse().isHavingElecIndex()) {
            if (startElecNumber == null)
                throw new AppException(ErrorCode.CONTRACT_NOT_ENTER_START_ELEC_RECORD);
            if (!room.validateElecIndex(startDate, startElecNumber))
                throw new AppException(ErrorCode.ELEC_NUMBER_NOT_VALID);

            if (endElecNumber != null) {
                if (endElecNumber < startElecNumber)
                    throw new AppException(ErrorCode.ELEC_NUMBER_NOT_VALID);
                if (!room.validateElecIndex(endDate, endElecNumber))
                    throw new AppException(ErrorCode.ELEC_NUMBER_NOT_VALID);
            }
        } else {
            startElecNumber = null;
            endElecNumber = null;
        }

        if (room.getHouse().isHavingWaterIndex()) {
            if (startWaterNumber == null)
                throw new AppException(ErrorCode.CONTRACT_NOT_ENTER_START_WATER_RECORD);
            System.out.println(1);
            if (!room.validateWaterIndex(startDate, startWaterNumber))
                throw new AppException(ErrorCode.WATER_NUMBER_NOT_VALID);
            System.out.println(2);
            if (endWaterNumber != null) {
                if (endWaterNumber < startWaterNumber)
                    throw new AppException(ErrorCode.WATER_NUMBER_NOT_VALID);
                System.out.println(3);
                if (!room.validateWaterIndex(endDate, endWaterNumber))
                    throw new AppException(ErrorCode.WATER_NUMBER_NOT_VALID);
            }
            System.out.println(4);
        } else {
            startWaterNumber = null;
            endWaterNumber = null;
        }

    }

    @PreRemove
    private void preRemove() {
        if (invoices != null && invoices.size() > 0)
            throw new AppException(ErrorCode.NOT_DELETE_CONTRACT);
    }

    public void deleteInvoice(Invoice invoice) {
        invoices.remove(invoice);
    }


    public boolean isIntersecting(Contract contract) {
        if (contract.getEndDate() != null && contract.endDate.isBefore(startDate)) return false;
        if (endDate != null && endDate.isBefore(contract.startDate)) return false;
        return true;
    }

    int initializeUnpaidInvoiceCount() {
        return (int) invoices.stream()
                .filter(invoice -> invoice.getStatus() == PaymentStatus.UNPAID)
                .count();
    }

    boolean initializeHavingCheckoutInvoice() {
        if (endDate == null) return false;
        return invoices.stream()
                .anyMatch(invoice -> invoice.getEndDate().equals(endDate));
    }

    public ContractStatus initializeStatus() {
        if (endDate == null) return ContractStatus.ACTIVE;

        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        if (daysBetween >= 30) return ContractStatus.ACTIVE;
        if (daysBetween > 0) return ContractStatus.SOON_EXPIRED;
        if (!havingCheckoutInvoice) return ContractStatus.PENDING_CHECKOUT_OR_INVOICE;
        if (endDate.isBefore(LocalDate.now())) return ContractStatus.EXPIRED;

        return ContractStatus.ACTIVE;
    }


    public PaymentStatus initializePaymentStatus() {
        if (unpaidInvoiceCount > 0) return PaymentStatus.UNPAID;
        return PaymentStatus.PAID;
    }

    public boolean contain(LocalDate month) {
        var startOfMonth = DateUtils.startOfMonth(month);
        var endOfMonth = DateUtils.endOfMonth(month);

        if (endDate != null && endDate.isBefore(startOfMonth)) return false;
        if (endOfMonth.isBefore(startDate)) return false;
        return true;
    }

}
