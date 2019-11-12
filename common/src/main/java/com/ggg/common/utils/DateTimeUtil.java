package com.ggg.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Mai Huu Hanh on 9/7/17.
 */

public class DateTimeUtil {

    public static final String DATE_TIME = "yyyy-MM-dd";
    public static final String DATE_TIME_HOUR = "HH:mm";
    public static final String DATE_TIME0 = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME1 = "MM/dd/yyyy      HH:mm";
    public static final String DATE_TIME_MONTH = "MM-dd HH:mm:ss";
    public static final String DATE_Calendar = "yyyyMMdd";
    public static final String DATE_TIME_MILL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_MILL_ZONE = "yyyy-MM-dd HH:mm:ss Z";
    public static final String DATE_TIME_MILL_HOUR_MIN_MILL = "HH:mm:ss";
    public static final String T_HOUR = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String HR_HOUR_SERVER = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";
    public static final String HR_HOUR_CLIENT = "dd-MM-yyyy";


    public static String baseTimeZone = "-7:00";
    public static String dateFormat = "dd-MM-yy";

    private DateTimeUtil() {
    }

    public static String changeDateFormat(String dateFormat1, String dateFormat2, String dateString) {
        String result = dateString;
        if (dateString == null || dateString.length() <= 0){
            return "";
        }
        SimpleDateFormat srcDf = new SimpleDateFormat(dateFormat1);
        SimpleDateFormat destDf = new SimpleDateFormat(dateFormat2);
        try {
            Date date = srcDf.parse(dateString);
            result = destDf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long convertTimeToMiliSecond(String dateFormat,String time) {
//        String timeZone = PrefsUtil.getKeynameBasetimezone();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
//        TimeZone tz = TimeZone.getTimeZone("GMT" + timeZone);
//        format.setTimeZone(tz);
        Long timeMilisecond = Long.valueOf(0);
        Date date = null;
        try {
            date = format.parse(time);
            timeMilisecond = date.getTime();
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeMilisecond;
    }

    public static long convertYearMonthDaytoMilisecond(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTimeInMillis();
    }

    public static Calendar getCalendarFromYearMonthDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    public static String convertDateTimetoDisplay(String dateFormat, String time) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(time);
    }


    public static String convertMilisecondToDate(String dateFormat, long milisecond) {
        Date date = new Date(convertMiliSecondFromServerToClient(milisecond));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public static String convertSecondToDisplay(Long second) {
        if (second == null) {
            second = 0L;
        }
        Long timeClient = convertMiliSecondFromServerToClient(second);
        Date date = new Date(timeClient);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public static Long convertMiliSecondFromClientToServer(long miliSecond) {
        Date dateTime = new Date(miliSecond);
        return convertDateFromClientToServer(baseTimeZone, dateTime);
    }

    public static Long convertMiliSecondFromServerToClient(long miliSecond) {
        final String formatPattern = DATE_TIME_MILL;
        final SimpleDateFormat formatServer = new SimpleDateFormat(formatPattern);
        final SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        TimeZone tz = TimeZone.getTimeZone("GMT" + baseTimeZone);
        formatServer.setTimeZone(tz);
        Date dateTime = new Date(miliSecond);
        String serverTimeString = formatServer.format(dateTime);
        try {
            Date serverTime = format.parse(serverTimeString);
            return serverTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.valueOf(0);
    }

    public static String nonConvertSecondToDisplay(Long second) {
        Date date = new Date(second);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public static String convertYearMonthDaytoDisplay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(calendar.getTime());
    }

    public static long convertDateStringToMilisecond(String dateFormat, String dateString) {
        DateFormat format = new SimpleDateFormat(dateFormat);
        Long milisecond = Long.valueOf(0);
        try {
            Date date = format.parse(dateString);
            milisecond = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milisecond;
    }

    public static Long convertDateFromClientToServer(String timeZone, Date date) {
        final String formatPattern = DATE_TIME_MILL;
        final String formatTimeZonePattern = DATE_TIME_MILL_ZONE;
        final SimpleDateFormat formatServer = new SimpleDateFormat(formatPattern);
        final SimpleDateFormat format = new SimpleDateFormat(formatTimeZonePattern);
        String timeZoneBase = timeZone.replace(":", "");
        String serverTimeString = formatServer.format(date);
        serverTimeString = serverTimeString + " " + timeZoneBase;
        try {
            Date serverTime = format.parse(serverTimeString);
            return serverTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.valueOf(0);
    }

    public static String getCurrentDateTime() {
        return getCurrentDateTime(DATE_TIME_MILL);
    }

    public static String getCurrentDateTime(String format) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Calendar getCalendarFromSecond(Long second) {
        Calendar calendar = Calendar.getInstance();
        if (second > Long.valueOf(0L)) {
            calendar.setTimeInMillis(second);
        }
        return calendar;
    }

    public static String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(date);
    }
}
