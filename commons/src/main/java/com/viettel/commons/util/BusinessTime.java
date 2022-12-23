package com.viettel.commons.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class BusinessTime {
    private static final ZoneId BUSINESS_ZONE_ID = ZoneOffset.UTC;

    public static LocalDate toBusinessDate(OffsetDateTime dateTime) {
        return LocalDate.ofInstant(dateTime.toInstant(), BUSINESS_ZONE_ID);
    }

    public static String format(OffsetDateTime dateTime, DateTimeFormatter formatter) {
        return formatter.format(dateTime.atZoneSameInstant(BUSINESS_ZONE_ID));
    }
}
