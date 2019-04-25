package com.zhongwang.sale.utils;

public class Utils {


    // 小数点后两位小数，大于两位时返回false。
    public static boolean checkPrice(String number) {
        if (number.contains(".")) {
            String intNumber = number.substring(number.indexOf("."));
            if (intNumber.length() > 3) {
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
