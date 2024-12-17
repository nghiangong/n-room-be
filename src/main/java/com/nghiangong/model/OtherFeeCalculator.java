package com.nghiangong.model;

import java.time.LocalDate;
import java.util.List;

import com.nghiangong.dto.request.invoice.CheckoutInvoiceReq;
import com.nghiangong.entity.House;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.entity.room.Room;
import com.nghiangong.service.ElecWaterRecordService;

public class OtherFeeCalculator {
    public static void calculate(List<Invoice> invoices) {
        invoices.forEach(invoice -> calculate(invoice));
    }

    public static void calculate(Invoice invoice) {
        int lengthOfMonth = invoice.getStartDate().lengthOfMonth();
        int daysStayed = (int)
                (invoice.getEndDate().toEpochDay() - invoice.getStartDate().toEpochDay() + 1);
        double ratio = (double) daysStayed / lengthOfMonth;
        Contract contract = invoice.getContract();
        for (var otherFee : contract.getRoom().getHouse().getOtherFees()) {
            var newInvoiceItem = InvoiceItem.builder()
                    .name(otherFee.getName())
                    .unitPrice(otherFee.getPrice())
                    .invoice(invoice)
                    .build();
            switch (otherFee.getUnit()) {
                case PER_ROOM -> {
                    newInvoiceItem.setNote("Số ngày ở: " + daysStayed + "/" + lengthOfMonth);
                    newInvoiceItem.setUnit("phòng/tháng");
                    newInvoiceItem.setAmount((int) (otherFee.getPrice() * ratio));
                }
                case PER_PERSON -> {
                    newInvoiceItem.setNote("Số ngày ở: " + daysStayed + "/" + lengthOfMonth
                                           + "; Số người ở: " + contract.getNumberOfPeople());
                    newInvoiceItem.setUnit("người/tháng");
                    newInvoiceItem.setAmount((int) (otherFee.getPrice() * contract.getNumberOfPeople() * ratio));
                }
                case PER_BICYCLE -> {
                    newInvoiceItem.setNote("Số ngày ở: " + daysStayed + "/" + lengthOfMonth + "; Số xe đạp: "
                                           + contract.getNumberOfBicycle());
                    newInvoiceItem.setUnit("xe/tháng");
                    newInvoiceItem.setAmount((int) (otherFee.getPrice() * contract.getNumberOfBicycle() * ratio));
                }
                case PER_MOTORBIKE -> {
                    newInvoiceItem.setNote("Số ngày ở: " + daysStayed + "/" + lengthOfMonth + "; Số xe máy: "
                                           + contract.getNumberOfMotorbike());
                    newInvoiceItem.setUnit("xe/tháng");
                    newInvoiceItem.setAmount((int) (otherFee.getPrice() * contract.getNumberOfMotorbike() * ratio));
                }
            }
            invoice.addInvoiceItem(newInvoiceItem);
        }
    }
}
