package com.nghiangong.model.waterCalculator;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.entity.room.Room;
import com.nghiangong.service.ElecWaterRecordService;

public class WC4 {
    public static void calculate(ElecWaterRecordService service, List<Room> rooms, List<Invoice> invoices) {
        int unitPrice = rooms.get(1).getHouse().getWaterChargePerPerson();
        int lengthOfMonth = invoices.get(1).getCreateDate().lengthOfMonth();
        for (int i = 0; i < rooms.size(); i++) {
            int numberOfPeople = rooms.get(i).getCurrentContract().getNumberOfPeople();
            var invoice = invoices.get(i);
            int daysStayed = (int)
                    (invoice.getEndDate().toEpochDay() - invoice.getStartDate().toEpochDay() + 1);
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
}
