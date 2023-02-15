package com.hexagonstore.cash.utils;

import com.hexagonstore.cash.CashSpigot;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberFormatter {

    private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");

    private static List<String> suffixes;
    private static boolean enabled;

    static {
        EC_Config config = CashSpigot.getPlugin().getCfg();
        suffixes = config.getStringList("Formatter.suffixes");
        enabled = config.getBoolean("Formatter.ativar");
    }

    public static void changeSuffixes(List<String> suffixes) {
        NumberFormatter.suffixes = suffixes;
    }

    public static String formatNumber(double value) {
        if(enabled) {
            int index = 0;

            double tmp;
            while ((tmp = value / 1000) >= 1) {
                value = tmp;
                ++index;
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            return decimalFormat.format(value) + suffixes.get(index);
        }else return String.valueOf(value);
    }

    public static double parseString(String value) throws Exception {
        if(enabled) {
            try {
                return Double.parseDouble(value);
            } catch (Exception ignored) {}

            Matcher matcher = PATTERN.matcher(value);
            if (!matcher.find()) {
                throw new Exception("Invalid format");
            }

            double amount = Double.parseDouble(matcher.group(1));
            String suffix = matcher.group(2);

            int index = suffixes.indexOf(suffix.toUpperCase());
            return amount * Math.pow(1000, index);
        }else {
            try {
                return Double.parseDouble(value);
            } catch (Exception ignored) {
                return 0.0;
            }
        }
    }
}