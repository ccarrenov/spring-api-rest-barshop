package com.barshop.app.models.mapper.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public final class DecimalUtil {

    private DecimalUtil() throws InstantiationException {
        throw new InstantiationException("You can't create new instance of DecimalUtil.");
    }

    public static Double roundDecimal( double number, int decimal ) {
        StringBuilder sb = new StringBuilder("0").append((decimal > 0 ? "." : ""));
        for (int i = 0; i < decimal; i++) {
            sb.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(sb.toString());
        return Double.valueOf(decimalFormat.format(number));
    }

    public static Double roundDecimal( double number, int decimal, RoundingMode rounding ) {
        StringBuilder sb = new StringBuilder("0").append((decimal > 0 ? "." : ""));
        for (int i = 0; i < decimal; i++) {
            sb.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(sb.toString());
        decimalFormat.setRoundingMode(rounding);
        return Double.valueOf(decimalFormat.format(number));
    }
}
