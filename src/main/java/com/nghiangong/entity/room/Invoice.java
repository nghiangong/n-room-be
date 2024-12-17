package com.nghiangong.entity.room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.nghiangong.constant.InvoiceStatus;

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

    @Enumerated(EnumType.STRING)
    InvoiceStatus status = InvoiceStatus.UNPAID;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    List<InvoiceItem> invoiceItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "contract_id")
    Contract contract;

    public void addInvoiceItem(InvoiceItem invoiceItem) {
        invoiceItems.addLast(invoiceItem);
        amount += invoiceItem.getAmount();
    }
}
