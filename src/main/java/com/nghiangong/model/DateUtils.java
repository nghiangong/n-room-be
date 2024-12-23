package com.nghiangong.model;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
    public static LocalDate startOfMonth(LocalDate month) {
        return month.withDayOfMonth(1);
    }

    public static LocalDate endOfMonth(LocalDate month) {
        return month.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate endOfLastMonth(LocalDate date) {
        return date.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static boolean isEndOfLastMonth(LocalDate date) {
        LocalDate endOfLastMonth = date.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        return date.equals(endOfLastMonth);
    }

    public static boolean isSameMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth();
    }

    public static boolean remainingDateLessAMonth(LocalDate endDate) {
        LocalDate today = LocalDate.now();
        return endDate.minusMonths(1).isBefore(today);
    }

    public static LocalDate latestDate(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2) ? date1 : date2;
    }

    public static LocalDate earliestDate(LocalDate date1, LocalDate date2) {
        if (date1 == null) return date2;
        if (date2 == null) return date1;
        return date1.isBefore(date2) ? date1 : date2;
    }
}
