package com.github.leandrolimasi.util;

import com.github.leandrolimasi.exception.LogParserException;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by leandrolimadasilva on 26/11/2017.
 */
public final class Utils {

    public static final String DATETIME_FORMATTER = "yyyy-MM-dd.HH:mm:ss";

    private static final String IPV4_PATTERN_STRING=
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_PATTERN_STRING);

    /** Parse String to Date
     *
     * @param dateStr
     * @return
     */
    public static Optional<Date> parseDate(String dateStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMATTER);
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
        return Optional.of(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    /** Validate Ip Address
     *
     * @param ipStr
     * @return
     * @throws LogParserException
     */
    public static String validateIpv4Address(String ipStr) throws LogParserException{
        if (IPV4_PATTERN.matcher(ipStr).matches()) {
            return ipStr;
        } else {
            throw new LogParserException("invalid IP address");
        }
    }

}
