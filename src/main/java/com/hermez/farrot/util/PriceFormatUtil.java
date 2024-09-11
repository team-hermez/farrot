package com.hermez.farrot.util;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatUtil {

    public static String formatPrice(Integer price) {
        if (price == null) {
            return "0";
        }
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(price);
    }
}