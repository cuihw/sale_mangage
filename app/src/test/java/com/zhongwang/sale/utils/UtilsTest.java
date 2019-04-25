package com.zhongwang.sale.utils;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void checkPrice() {
        String price = "12.22";
        boolean b = Utils.checkPrice(price);
        System.out.println("b = " + b);
        Assert.assertTrue(b);

        price = "12.220";
        b = Utils.checkPrice(price);
        System.out.println("b = " + b);
        Assert.assertFalse(b);

        price = "12.2";
        b = Utils.checkPrice(price);
        System.out.println("b = " + b);
        Assert.assertEquals(true, b);

        price = "12.";
        b = Utils.checkPrice(price);
        System.out.println("b = " + b);
        Assert.assertEquals(true, b);

        price = "12";
        b = Utils.checkPrice(price);
        System.out.println("b = " + b);
        Assert.assertEquals(true, b);
    }

    @Test
    public void transform2Decimal() {
        String t = Utils.transform2Decimal("333");
        System.out.println("t = " + t);

        t = Utils.transform2Decimal("333.2222");
        System.out.println("t = " + t);

        t = Utils.transform2Decimal("333.2");
        System.out.println("t = " + t);

        t = Utils.transform2Decimal("333.");
        System.out.println("t = " + t);
    }
}