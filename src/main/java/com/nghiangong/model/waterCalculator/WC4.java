package com.nghiangong.model.waterCalculator;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.service.ElecWaterRecordService;

public class WC4 {
    public static void calculate(ElecWaterRecordService service, List<Invoice> invoices) {
        invoices.forEach(invoice -> calculate(service, invoice));
    }

    public static void calculate(ElecWaterRecordService service, Invoice invoice) {
        var contract = invoice.getContract();
        var room = contract.getRoom();
        var house = room.getHouse();

        int lengthOfMonth = contract.getStartDate().lengthOfMonth();
        int numberOfPeople = contract.getNumberOfPeople();
        int daysStayed = (int)
                (invoice.getEndDate().toEpochDay() - invoice.getStartDate().toEpochDay() + 1);
        int unitPrice = house.getWaterChargePerPerson();
        var newInvoiceItem = InvoiceItem.builder()
                .name("Tiền nước")
                .note("Số ngày ở: " + daysStayed + "/" + lengthOfMonth + ", Số người ở: " + numberOfPeople)
                .unitPrice(unitPrice)
                .unit("người/tháng")
                .amount(unitPrice * numberOfPeople * daysStayed / lengthOfMonth)
                .invoice(invoice)
                .build();
        invoice.addInvoiceItem(newInvoiceItem);
    }
}
