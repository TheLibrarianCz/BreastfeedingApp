package com.example.kojeniapp.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM. yyyy");
    }

    public static DateTimeFormatter getTimeFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    public static int getDate(LocalDate localDate) {
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        return day + month * 100 + year * 10000;
    }

    public static int getDate() {
        LocalDate localDate = LocalDate.now();
        return getDate(localDate);
    }

    public static LocalDate getDate(int storedInt) {
        // 20230610 / 10000 = 2023,0610
        // 2023,0610 - 2023 * 100 = 06,10

        int year = storedInt / 10000;
        int month = (storedInt - (year * 10000)) / 100; // 610
        int day = storedInt - (year * 10000) - (month * 100);

        return LocalDate.of(year, month, day);
    }

    public static int getTimestamp() {
        LocalTime time = LocalTime.now();

        int hours = time.getHour();
        int minute = time.getMinute();

        return hours * 100 + minute;
    }

    public static LocalTime getTimestamp(int time) {
        int hour = time / 100;
        int minute = time % 100;

        return LocalTime.of(hour, minute);
    }
}
