package com.nghiangong.model;

import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.service.ElecWaterRecordService;

public class WaterCalculator {
    public static void calculate(ElecWaterRecordService service, List<Invoice> invoices) {
        invoices.forEach(invoice -> calculate(service, invoice));
    }

    public static void calculate(ElecWaterRecordService service, Invoice invoice) {
        if (invoice.getContract().getRoom().getHouse().isHavingWaterIndex())
            calculateWithIndex(service, invoice);
        else
            calculateWithoutIndex(invoice);
    }

    static void calculateWithoutIndex(Invoice invoice) {
        var contract = invoice.getContract();
        var room = contract.getRoom();
        var house = room.getHouse();

        int lengthOfMonth = contract.getStartDate().lengthOfMonth();
        int numberOfPeople = contract.getNumberOfPeople();
        int daysStayed = (int) (invoice.getEndDate().toEpochDay() - invoice.getStartDate().toEpochDay() + 1);
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

    static void calculateWithIndex(ElecWaterRecordService service, Invoice invoice) {
        var contract = invoice.getContract();
        var room = contract.getRoom();
        var house = room.getHouse();

        var waterPricePerUnit = house.getWaterPricePerUnit();

        int oldValue, newValue;
        if (invoice.getStartDate() == contract.getStartDate()) {
            if (contract.getStartWaterNumber() == null)
                throw new AppException(ErrorCode.START_WATER_NUMBER_NOT_VALID);
            oldValue = contract.getStartWaterNumber();
        } else
            oldValue = service.getWaterRecord(room.getId(), invoice.getStartDate());

        if (invoice.getEndDate() == contract.getEndDate()) {
            if (contract.getEndWaterNumber() == null)
                throw new AppException(ErrorCode.END_WATER_NUMBER_NOT_VALID);
            newValue = contract.getEndWaterNumber();
        } else
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
