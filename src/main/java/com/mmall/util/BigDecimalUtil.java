package com.mmall.util;

import java.math.BigDecimal;

/**
 * Created by bu_dong on 2017/7/29.
 */
public class BigDecimalUtil {

    public static BigDecimal add(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2);
    }

    public static BigDecimal sub(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2);
    }

    public static BigDecimal mul(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2);
    }

    public static BigDecimal div(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);//保留两位小数 四舍五入
    }
}
