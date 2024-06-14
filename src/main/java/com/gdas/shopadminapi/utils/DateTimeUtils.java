package com.gdas.shopadminapi.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtils {

    public static final ZoneId AMERICA_SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");

    private DateTimeUtils() {}

    public static LocalDate currentLocalDate() {
        return LocalDate.now(AMERICA_SAO_PAULO_ZONE);
    }

    public static LocalDateTime currentLocalDateTime() {
        return LocalDateTime.now(AMERICA_SAO_PAULO_ZONE);
    }

    public static LocalDate firstDayOfCurrentMonth() {
        return currentLocalDate()
                .withDayOfMonth(1);
    }

    public static LocalDate lastDayOfCurrentMonth() {
        return currentLocalDate()
                .plusMonths(1)
                .withDayOfMonth(1)
                .minusDays(1);
    }
}
