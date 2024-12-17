package com.nghiangong.model.elecCalculator;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.entity.room.Room;
import com.nghiangong.service.ElecWaterRecordService;

public class EC1 {
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
        int unitPrice = house.getElecCostByPeopleCount().get(numberOfPeople - 1);

        var newInvoiceItem = InvoiceItem.builder()
                .name("Tiền điện")
                .note("Số ngày ở: " + daysStayed + "/" + lengthOfMonth)
                .unitPrice(unitPrice)
                .unit("phòng/tháng")
                .amount(unitPrice * daysStayed / lengthOfMonth)
                .invoice(invoice)
                .build();
        invoice.addInvoiceItem(newInvoiceItem);
    }
}
