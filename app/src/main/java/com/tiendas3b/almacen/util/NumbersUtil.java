package com.tiendas3b.almacen.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Created by danflo on 12/10/2016 madafaka.
 */
public class NumbersUtil {

    private static final String THOUSANDS = "%,d";
    private static final String MONEY = "%,.2f";
    public static final String COCIENTE = "%,.1f";
    private static final Locale locale = Locale.getDefault();
//    private static final String MONEY_PATTERN = "[0-9]*.[0-9][0-9]";

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static String truncate2decimalsStr(double d) {
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(d);
    }

    public static double truncate2decimals(double d) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(d));
    }

    public static String thousandsSeparator(int number) {
        return format(locale, THOUSANDS, number);
    }

    public static String moneyFormat(Float number) {
//        return String.format(locale, MONEY, number == null ? 0.0f : number);
        return NumberFormat.getCurrencyInstance().format(number == null ? 0.0f : number);
    }

//    public static boolean validateMoney(final String amount) {
//        return validate(MONEY_PATTERN, amount);
//    }

    private static boolean validate(String pattern, String input) {
        return Pattern.compile(pattern).matcher(input).matches();
    }

    public static String moneyFormatCleanString(double parsed) {
        return NumberFormat.getCurrencyInstance().format((parsed/100.0));
    }
}
