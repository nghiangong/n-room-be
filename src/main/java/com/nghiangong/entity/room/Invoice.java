package com.nghiangong.entity.room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.nghiangong.constant.PaymentStatus;
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

    public void setPaid() {
        if (status == PaymentStatus.PAID) return;
        status = PaymentStatus.PAID;
    }

    public void addInvoiceItem(InvoiceItem invoiceItem) {
        invoiceItems.addLast(invoiceItem);
        amount += invoiceItem.getAmount();
    }
}
