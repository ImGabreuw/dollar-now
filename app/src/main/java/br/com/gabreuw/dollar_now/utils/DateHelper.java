package br.com.gabreuw.dollar_now.utils;

import android.annotation.SuppressLint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private DateHelper() {
    }

    @SuppressLint("NewApi")
    public static String format(LocalDate date) {
        return DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .format(date);
    }
}
