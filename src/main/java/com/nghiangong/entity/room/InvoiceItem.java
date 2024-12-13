package com.nghiangong.entity.room;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;
    String note;
    int unitPrice;
    String unit;
    int amount;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    Invoice invoice;
}
