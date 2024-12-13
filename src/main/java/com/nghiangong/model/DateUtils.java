package com.nghiangong.model;

import java.time.LocalDate;
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
}
