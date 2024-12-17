package com.nghiangong.model.waterCalculator;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.service.ElecWaterRecordService;

public class WC1 {
    public static void calculate(ElecWaterRecordService service, List<Invoice> invoices) {
        invoices.forEach(invoice -> calculate(service, invoice));

    }

    public static void calculate(ElecWaterRecordService service, Invoice invoice) {
        var contract = invoice.getContract();
        var room = contract.getRoom();
        var house = room.getHouse();

        var waterPricePerUnit = house.getElecPricePerUnit();

        int oldValue, newValue;
        if (invoice.getStartDate() == contract.getStartDate())
            oldValue = contract.getStartWaterNumber();
        else
            oldValue = service.getWaterRecord(room.getId(), invoice.getStartDate());
        if (invoice.isCheckout())
            newValue = contract.getEndWaterNumber();
        else
            newValue = service.getWaterRecord(room.getId(), invoice.getStartDate());

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
