package com.nghiangong.model.waterCalculator;

import java.util.ArrayList;
import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.entity.room.Room;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.DateUtils;
import com.nghiangong.service.ElecWaterRecordService;

public class WC2 {
    private static final int[] TIER_LIMITS = {4, 6};
    private static final int[] TIER_RATES = {6700, 12900, 14400};

    public static void calculate(ElecWaterRecordService service, List<Room> rooms, List<Invoice> invoices) {
        int total = 0;
        int peopleTotal = 0;
        List<Integer> oldValues = new ArrayList<Integer>();
        List<Integer> newValues = new ArrayList<Integer>();
        for (int i = 0; i < rooms.size(); i++) {
            var room = rooms.get(i);
            var invoice = invoices.get(i);
            int oldValue;
            if (DateUtils.isEndOfLastMonth(invoice.getStartDate()))
                oldValue = service.getWaterRecord(room.getId(), invoice.getStartDate());
            else oldValue = room.getCurrentContract().getStartWaterNumber();
            int newValue = service.getWaterRecord(room.getId(), invoice.getStartDate());

            oldValues.addLast(oldValue);
            newValues.addLast(newValue);
            if (newValue - oldValue < 0) throw new AppException(ErrorCode.WATER_NUMBER_NOT_VALID);
            total += newValue - oldValue;
        }

        int price = calculatePrice(total, peopleTotal);
        for (int i = 0; i < rooms.size(); i++) {
            var invoice = invoices.get(i);

            int oldValue = oldValues.get(i);
            int newValue = newValues.get(i);
            int quantity = newValue - oldValue;
            if (quantity < 0) throw new AppException(ErrorCode.WATER_NUMBER_NOT_VALID);

            var newInvoiceItem = InvoiceItem.builder()
                    .name("Tiền nước")
                    .note("Sử dụng: " + quantity + " số; "
                          + "Số cũ: " + oldValue + "; "
                          + "Số mới: " + newValue)
                    .unitPrice(price)
                    .unit("số nước")
                    .amount(quantity * price)
                    .invoice(invoice)
                    .build();
            invoice.addInvoiceItem(newInvoiceItem);
        }
    }

    static int calculatePrice(int usage, int numberOfPeople) {
        int averageUsage = usage / numberOfPeople;

        for (int i = 0; i < TIER_LIMITS.length; i++) if (averageUsage <= TIER_LIMITS[i]) return TIER_RATES[i];
        return TIER_RATES[TIER_RATES.length - 1];
    }
}
