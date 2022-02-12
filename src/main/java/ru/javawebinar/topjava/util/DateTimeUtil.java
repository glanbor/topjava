package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T checkedValue, @Nullable T startValue, @Nullable T endValue) {
        return (startValue == null || checkedValue.compareTo(startValue) >= 0)
                && (endValue == null || checkedValue.compareTo(endValue) < 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDateTime convertDateToDateTime(@Nullable LocalDate localDate, LocalDate defaultDate, LocalTime defaultTime) {
        if (localDate == null) {
            return LocalDateTime.of(defaultDate, defaultTime);
        }
        return LocalDateTime.of(localDate, defaultTime);
    }
}

