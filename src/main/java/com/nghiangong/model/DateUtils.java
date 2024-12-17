package com.nghiangong.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
    public static LocalDate endOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate endOfLastMonth(LocalDate date) {
        return date.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static boolean isEndOfLastMonth(LocalDate date) {
        LocalDate endOfLastMonth = date.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        return date.equals(endOfLastMonth);
    }

    public static LocalDate paymentCycle(LocalDate startDate) {
        LocalDate today = LocalDate.now();

        LocalDate nextPaymentCycle = startDate;
        while (nextPaymentCycle.isBefore(today) || nextPaymentCycle.isEqual(today)) {
            nextPaymentCycle = nextPaymentCycle.plusMonths(1);
        }
        nextPaymentCycle = nextPaymentCycle.minusDays(1);
        return nextPaymentCycle;
    }

    public static LocalDate nextPaymentCycle(LocalDate startDate) {
        LocalDate today = LocalDate.now();

        LocalDate nextPaymentCycle = startDate;
        while (nextPaymentCycle.isBefore(today) || nextPaymentCycle.isEqual(today)) {
            nextPaymentCycle = nextPaymentCycle.plusMonths(1);
        }
        nextPaymentCycle = nextPaymentCycle.plusMonths(1);
        nextPaymentCycle = nextPaymentCycle.minusDays(1);
        return nextPaymentCycle;
    }

    public static boolean remainingDateLessAMonth(LocalDate endDate) {
        LocalDate today = LocalDate.now();
        return endDate.minusMonths(1).isBefore(today);
    }
}
