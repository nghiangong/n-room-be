package com.nghiangong.model;

import java.time.LocalDate;
import java.util.List;

import com.nghiangong.entity.House;
import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.entity.room.Room;
import com.nghiangong.service.ElecWaterRecordService;

public class OtherFeeCalculator {
    public static void calculate(ElecWaterRecordService service, List<Room> rooms, List<Invoice> invoices) {
        int lengthOfMonth = invoices.get(1).getCreateDate().lengthOfMonth();
        for (int i = 0; i < rooms.size(); i++) {
            var room = rooms.get(i);
            var invoice = invoices.get(i);
            var contract = room.getCurrentContract();
            int daysStayed = (int)
                    (invoice.getEndDate().toEpochDay() - invoice.getStartDate().toEpochDay() + 1);
            double ratio = (double) daysStayed / lengthOfMonth;
            for (var otherFee : room.getHouse().getOtherFees()) {
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
}
