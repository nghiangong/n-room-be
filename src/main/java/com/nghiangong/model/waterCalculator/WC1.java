package com.nghiangong.model.waterCalculator;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.entity.room.Room;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.DateUtils;
import com.nghiangong.service.ElecWaterRecordService;

public class WC1{
    public static void calculate(ElecWaterRecordService service, List<Room> rooms, List<Invoice> invoices) {
        var waterPricePerUnit = rooms.get(1).getHouse().getWaterPricePerUnit();
        for (int i = 0; i < rooms.size(); i++) {
            var room = rooms.get(i);
            var invoice = invoices.get(i);

            int oldValue;
            if (DateUtils.isEndOfLastMonth(invoice.getStartDate()))
                oldValue = service.getWaterRecord(room.getId(), invoice.getStartDate());
            else oldValue = room.getCurrentContract().getStartWaterNumber();
            int newValue = service.getWaterRecord(room.getId(), invoice.getStartDate());

            int quantity = newValue - oldValue;
            if (quantity < 0) throw new AppException(ErrorCode.WATER_NUMBER_NOT_VALID);
            var newInvoiceItem = InvoiceItem.builder()
                    .name("Tiền nước")
                    .note("Sử dụng: " + quantity + " số; "
                            + "Số cũ: " + oldValue + "; "
                            + "Số mới: " + newValue)
                    .unitPrice(waterPricePerUnit)
                    .unit("số nước")
                    .amount(quantity * waterPricePerUnit)
                    .invoice(invoice)
                    .build();
            invoice.addInvoiceItem(newInvoiceItem);
        }
    }
}
