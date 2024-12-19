package com.nghiangong.model;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;

public class BaseRentCalculator {

    public static void calculate(List<Invoice> invoices) {
        invoices.forEach(invoice -> calculate(invoice));
    }

    public static void calculate(Invoice invoice) {
        int lengthOfMonth = invoice.getStartDate().lengthOfMonth();
        var rentPrice = invoice.getContract().getRentPrice();
        int daysStayed = (int)
                (invoice.getEndDate().toEpochDay() - invoice.getStartDate().toEpochDay() + 1);
        var newInvoiceItem = InvoiceItem.builder()
                .name("Tiền phòng")
                .note("Số ngày ở: " + daysStayed + "/" + lengthOfMonth)
                .unitPrice(rentPrice)
                .unit("phòng/tháng")
                .amount(rentPrice * daysStayed / lengthOfMonth)
                .invoice(invoice)
                .build();
        invoice.addInvoiceItem(newInvoiceItem);
    }

}
