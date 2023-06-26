package br.com.gabreuw.dollar_now.helper;

import java.util.Locale;

public class NumberHelper {

    private NumberHelper() {
    }

    public static Float parse(String raw) {
        try {
            return Float.parseFloat(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String format(Float number) {
        return String.format(
                Locale.US,
                "%.2f",
                number
        );
    }
}
