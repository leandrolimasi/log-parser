package com.github.leandrolimasi.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

/**
 * Created by leandrolimadasilva on 26/11/2017.
 */
public final class Utils {

    public static final String DATETIME_FORMATTER = "yyyy-MM-dd.HH:mm:ss";

    /**
     *
     * @param dateStr
     * @return
     */
    public static Optional<Date> parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMATTER);
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
        return Optional.of(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

    }

}
