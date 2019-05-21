package com.zhongwang.sale.utils;

public class Utils {


    // 小数点后三位小数，大于三位时返回false。
    public static boolean checkPrice(String number) {
        if (number.contains(".")) {
            String intNumber = number.substring(number.indexOf("."));
            if (intNumber.length() > 4) {
                return false;
            }
        }
        return true;
    }


    // 小数点后length位小数，大于length位时返回false。
    public static boolean checkPrice(String number, int length) {
        if (number.contains(".")) {
            String intNumber = number.substring(number.indexOf("."));
            if (intNumber.length() > (length + 1)) {
                return false;
            }
        }
        return true;
    }

    public static String transform2Decimal(String priceString) {
        if (isEmpty(priceString)) return "0.00";

        try {
            Float v = Float.parseFloat(priceString);
            return String.format("%1.2f", v);
        } catch (Exception e) {
            return "0.00";
        }
    }

    public static boolean isEmpty(String cs) {
        return cs == null || cs.trim().length() == 0;
    }
}
