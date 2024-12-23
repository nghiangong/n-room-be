package com.nghiangong.entity.room;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.nghiangong.constant.PaymentStatus;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.*;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;
    LocalDate startDate;
    LocalDate endDate;
    LocalDate createDate;
    int amount = 0;

    boolean checkout;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    PaymentStatus status = PaymentStatus.UNPAID;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    List<InvoiceItem> invoiceItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "contract_id")
    Contract contract;

    public Invoice(Contract contract, LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = DateUtils.endOfMonth(month);

        this.contract = contract;
        createDate = LocalDate.now();
        startDate = DateUtils.latestDate(startOfMonth, contract.getStartDate());
        endDate = DateUtils.earliestDate(endOfMonth, contract.getEndDate());
        name = "Hóa đơn tháng " + month.format(DateTimeFormatter.ofPattern("MM/YYYY"));

        BaseRentCalculator.calculate(this);
        OtherFeeCalculator.calculate(this);
        ElecCalculator.calculate(this);
        WaterCalculator.calculate(this);
    }

    public void setPaid() {
        if (status == PaymentStatus.PAID) return;
        status = PaymentStatus.PAID;
    }

    public void addInvoiceItem(InvoiceItem invoiceItem) {
        invoiceItems.addLast(invoiceItem);
        amount += invoiceItem.getAmount();
    }

    @PreRemove
    private void preRemove() {
        if (status == PaymentStatus.PAID)
            throw new AppException(ErrorCode.NOT_DELETE_INVOICE);
    }
}
