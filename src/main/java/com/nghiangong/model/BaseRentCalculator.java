package com.nghiangong.model;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.entity.room.Room;
import com.nghiangong.service.ElecWaterRecordService;

public class BaseRentCalculator {

    public static void calculate(ElecWaterRecordService service, List<Room> rooms, List<Invoice> invoices) {
        int lengthOfMonth = invoices.get(1).getCreateDate().lengthOfMonth();
        for (int i = 0; i < rooms.size(); i++) {
            var invoice = invoices.get(i);
            var rentPrice = rooms.get(i).getCurrentContract().getRentPrice();
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
}
