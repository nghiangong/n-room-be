package com.nghiangong.model.electricityCalculator;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.service.ElecWaterRecordService;

public class EC2 {
    public static void calculate(ElecWaterRecordService service, List<Invoice> invoices) {
        invoices.forEach(invoice -> calculate(service, invoice));

    }

    public static void calculate(ElecWaterRecordService service, Invoice invoice) {
        var contract = invoice.getContract();
        var room = contract.getRoom();
        var house = room.getHouse();

        var elecPricePerUnit = house.getElecPricePerUnit();

        int oldValue, newValue;
        if (invoice.getStartDate() == contract.getStartDate())
            oldValue = contract.getStartElecNumber();
        else
            oldValue = service.getElecRecord(room.getId(), invoice.getStartDate());
        if (invoice.isCheckout())
            newValue = contract.getEndELecNumber();
        else
            newValue = service.getElecRecord(room.getId(), invoice.getStartDate());

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
