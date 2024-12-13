package com.nghiangong.model.elecCalculator;

import java.util.ArrayList;
import java.util.List;

import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.InvoiceItem;
import com.nghiangong.entity.room.Room;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.DateUtils;
import com.nghiangong.service.ElecWaterRecordService;

public class EC3 {
    private static final int[] TIER_LIMITS = {50, 100, 200, 300, 400};
    private static final int[] TIER_RATES = {1728, 1786, 2074, 2612, 2919, 3015};

    public static void calculate(ElecWaterRecordService service, List<Room> rooms, List<Invoice> invoices) {
        int total = 0;
        List<Integer> oldValues = new ArrayList<Integer>();
        List<Integer> newValues = new ArrayList<Integer>();
        for (int i = 0; i < rooms.size(); i++) {
            var room = rooms.get(i);
            var invoice = invoices.get(i);
            int oldValue;
            if (DateUtils.isEndOfLastMonth(invoice.getStartDate()))
                oldValue = service.getElecRecord(room.getId(), invoice.getStartDate());
            else oldValue = room.getCurrentContract().getStartElecNumber();
            int newValue = service.getElecRecord(room.getId(), invoice.getStartDate());

            oldValues.addLast(oldValue);
            newValues.addLast(newValue);
            if (newValue - oldValue < 0) throw new AppException(ErrorCode.ELEC_NUMBER_NOT_VALID);
            total += newValue - oldValue;
        }

        int averagePrice = calculateAveragePrice(total);

        for (int i = 0; i < rooms.size(); i++) {
            var invoice = invoices.get(i);

            int oldValue = oldValues.get(i);
            int newValue = newValues.get(i);
            int quantity = newValue - oldValue;
            if (quantity < 0) throw new AppException(ErrorCode.ELEC_NUMBER_NOT_VALID);

            var newInvoiceItem = InvoiceItem.builder()
                    .name("Tiền điện")
                    .note("Sử dụng: " + quantity + " số; "
                            + "Số cũ: " + oldValue + "; "
                            + "Số mới: " + newValue)
                    .unitPrice(averagePrice)
                    .unit("số điện")
                    .amount(quantity * averagePrice)
                    .invoice(invoice)
                    .build();
            invoice.addInvoiceItem(newInvoiceItem);
        }
    }

    static int calculateAveragePrice(int usage) {
        int bill = 0;
        int remainingUsage = usage;

        for (int i = 0; i < TIER_LIMITS.length; i++) {
            int currentTierUsage = Math.min(remainingUsage, TIER_LIMITS[i] - (i == 0 ? 0 : TIER_LIMITS[i - 1]));
            bill += currentTierUsage * TIER_RATES[i];
            remainingUsage -= currentTierUsage;

            if (remainingUsage <= 0) {
                break;
            }
        }

        if (remainingUsage > 0) {
            bill += remainingUsage * TIER_RATES[TIER_RATES.length - 1];
        }
        return bill / usage;
    }
}
