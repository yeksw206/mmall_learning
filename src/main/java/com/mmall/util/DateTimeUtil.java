package com.mmall.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by bu_dong on 2017/7/26.
 */
public class DateTimeUtil {

    public static final String STARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date strToDate(String dateTimeStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);

        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STARD_FORMAT);

        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date, String formatStr){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static String dateToStr(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STARD_FORMAT);
    }
}
