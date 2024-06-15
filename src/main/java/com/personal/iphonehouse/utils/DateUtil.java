package com.personal.iphonehouse.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.*;

public class DateUtil {

    public static final int OneSecond = 1000;
    public static final int OneMinute = OneSecond * 60;
    public static final int OneHour = OneMinute * 60;
    public static final long OneDay = OneHour * 24;
    public static final String ZONA_ARG = "America/Argentina/Buenos_Aires";
    public static final String ZONA_UTC = "UTC";

    public Timestamp timeNow() {
        // ZonedDateTime _z_nowInBsAs = ZonedDateTime.now(ZoneId.of(ZONA_ARG));
        LocalDateTime nowInBsAs = LocalDateTime.now();
        return Timestamp.valueOf(nowInBsAs);
    }

    public java.sql.Date sqlDateNow() {
        // ZonedDateTime _z_nowInBsAs = ZonedDateTime.now(ZoneId.of(ZONA_ARG));
        LocalDate nowInBsAs = LocalDate.now();
        return Date.valueOf(nowInBsAs);
    }

    public java.sql.Timestamp sqlTimestampDateNow() {
        LocalDateTime nowInBs = LocalDateTime.now();
        return Timestamp.valueOf(nowInBs);
    }

    public java.util.Date utilDateNow() {
        // ZonedDateTime _z_nowInBsAs = ZonedDateTime.now(ZoneId.of(ZONA_ARG));
        LocalDate nowInBsAs = LocalDate.now();
        return new java.util.Date(Date.valueOf(nowInBsAs).getTime());
    }


    public java.util.Date dateNowInArgentina() {
        ZoneId nowInBsAs = ZoneId.of(ZONA_ARG);
        ZonedDateTime now = ZonedDateTime.now(nowInBsAs);
        return Date.from(now.toInstant());
    }

    public LocalDateTime convertJavaDateToLocalDateTime(java.util.Date date) {
        if (date != null) {
            Instant instant = date.toInstant();
            return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }
}