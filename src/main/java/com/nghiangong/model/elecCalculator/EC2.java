package com.nghiangong.model.electricityCalculator;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.entity.room.Room;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.DateUtils;
import com.nghiangong.service.ElecWaterRecordService;

public class EC2 {
    public static void calculate(ElecWaterRecordService service, List<Room> rooms, List<Invoice> invoices) {
        var elecPricePerUnit = rooms.get(1).getHouse().getElecPricePerUnit();
        for (int i = 0; i < rooms.size(); i++) {
            var room = rooms.get(i);
            var invoice = invoices.get(i);

            int oldValue;
            if (DateUtils.isEndOfLastMonth(invoice.getStartDate()))
                oldValue = service.getElecRecord(room.getId(), invoice.getStartDate());
            else oldValue = room.getCurrentContract().getStartElecNumber();
            int newValue = service.getElecRecord(room.getId(), invoice.getStartDate());

            int quantity = newValue - oldValue;
            if (quantity < 0) throw new AppException(ErrorCode.ELEC_NUMBER_NOT_VALID);

            var newInvoiceItem = InvoiceItem.builder()
                    .name("Tiền điện")
                    .note("Sử dụng: " + quantity + " số; "
                          + "Số cũ: " + oldValue + "; "
                          + "Số mới: " + newValue)
                    .unitPrice(elecPricePerUnit)
                    .unit("số điện")
                    .amount(quantity * elecPricePerUnit)
                    .invoice(invoice)
                    .build();
            invoice.addInvoiceItem(newInvoiceItem);
        }
    }
}
