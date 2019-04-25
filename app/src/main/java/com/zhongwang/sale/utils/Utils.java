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
}
