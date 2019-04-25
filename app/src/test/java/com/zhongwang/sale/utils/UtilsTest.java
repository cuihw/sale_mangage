package com.zhongwang.sale.utils;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void checkPrice() {
        String price = "12.22";
        boolean b = Utils.checkPrice(price);
        System.out.println("b = " + b);
        price = "12.220";
        b = Utils.checkPrice(price);
        System.out.println("b = " + b);
        price = "12.2";
        b = Utils.checkPrice(price);
        System.out.println("b = " + b);
        price = "12.";
        b = Utils.checkPrice(price);
        System.out.println("b = " + b);
        price = "12";
        b = Utils.checkPrice(price);
        System.out.println("b = " + b);
    }
}