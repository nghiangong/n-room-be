package com.nghiangong.model;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.service.ElecWaterRecordService;

import java.util.List;

public class ElecCalculator {
    public static void calculate(ElecWaterRecordService service, List<Invoice> invoices) {
        invoices.forEach(invoice -> calculate(service, invoice));
    }

    public static void calculate(ElecWaterRecordService service, Invoice invoice) {
        if (invoice.getContract().getRoom().getHouse().isHavingElecIndex())
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

    static void calculateWithIndex(ElecWaterRecordService service, Invoice invoice) {
        var contract = invoice.getContract();
        var room = contract.getRoom();
        var house = room.getHouse();

        var elecPricePerUnit = house.getElecPricePerUnit();

        int oldValue, newValue;
        if (invoice.getStartDate() == contract.getStartDate()) {
            if (contract.getStartElecNumber() == null)
                throw new AppException(ErrorCode.START_ELEC_NUMBER_NOT_VALID);
            oldValue = contract.getStartElecNumber();
        } else
            oldValue = service.getElecRecord(room.getId(), invoice.getStartDate());

        if (invoice.getEndDate() == contract.getEndDate()) {
            if (contract.getEndElecNumber() == null)
                throw new AppException(ErrorCode.END_ELEC_NUMBER_NOT_VALID);
            newValue = contract.getEndElecNumber();
        } else
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
