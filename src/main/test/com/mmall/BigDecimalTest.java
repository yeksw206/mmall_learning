package com.mmall;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by bu_dong on 2017/7/29.
 */
public class BigDecimalTest {

    @Test
    public void test1(){
        System.out.println(0.05+0.01);
        System.out.println(1.0-0.32);
        System.out.println(4.05*100);
        System.out.println(123.8/100);
    }

    @Test
    public void test2(){
        BigDecimal a1 = new BigDecimal("4.054");
        BigDecimal a2 = new BigDecimal("3.39");
        System.out.print(a1.add(a2));

    }
}
