package com.tiendas3b.almacen.util;

import android.util.Log;

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by dfa on 04/04/2016.
 */
public class DateUtil {

    public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
    public static final String DD_MM_YYYY_HH_MM = "dd-MM-yyyy HH:mm";
    public static final String DD_MMMM_YYYY = "dd MMMM yyyy";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final java.lang.String YY_MM_DD = "yy-MM-dd";
    public static final String HH_MM = "HH:mm";
    public static final String MM_SS = "mm:ss";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String DD_MM = "dd-MM";
    private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    private static final String SEPARATOR_ = "-";
    private static final String TAG = "DateUtil";

    public static Date getDate(String dateStr) throws ParseException {
        return getDate(dateStr, YYYY_MM_DD);
    }

    public static Date getDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.parse(dateStr);
    }

    public static String getTodayStr() {
        return getDateStr(new Date());
    }

    public static String getDateStr(Date date) {
        return getDateStr(date, YYYY_MM_DD);
    }

    public static String getTodayWithTimeStr() {
        return getDateStr(new Date(), DD_MM_YYYY_HH_MM);
    }

    private static String getDateStr(Date date, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }

    public static String getDateStr(long millis) {
        return getDateStr(new Date(millis), YYYY_MM_DD);
    }

    public static String getDateStr(long millis, String format) {
        return getDateStr(new Date(millis), format);
    }

    public static String getDateStr(int year, int month, int day) {
        return getDateStr(year, month, day, SEPARATOR_);
    }

    private static String getDateStr(int year, int month, int day, String separator) {
        return year + separator + completeZeroStr(month) + separator + completeZeroStr(day);
    }

    private static String completeZeroStr(int month) {
        return month > 9 ? String.valueOf(month) : ("0" + month);
    }

    public static String getTimeStr(int hour, int minute) {
        String hourStr = hour < 10 ? "0" + hour : String.valueOf(hour);
        String minStr = minute < 10 ? "0" + minute : String.valueOf(minute);
        return hourStr + ":" + minStr;
    }

    public static boolean after(String initialTime, String finalTime) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat(HH_MM, Locale.getDefault());
        return parser.parse(initialTime).after(parser.parse(finalTime));
    }

    public static Date getDateWithoutTime(Date date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(YYYY_MM_DD, Locale.getDefault());
        return formatter.parse(formatter.format(date));
    }

    public static String getTimeNowStr() {
        return getTimeStr(new Date());
    }


    public static String getTimeStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(HH_MM, Locale.getDefault());
        return sdf.format(date);
    }

    public static String getTimeStr(long millis) {
        return getTimeStr(new Date(millis));
    }

    public static Date getTime(int hour, int minute) throws ParseException {
        return getTimeWithoutDate(hour + ":" + minute);
    }

    public static Date getTime(String hour, String minute) throws ParseException {
        return getTimeWithoutDate(hour + ":" + minute);
    }

    public static Date getTime(String hourMinute) throws ParseException {
        return getTimeWithoutDate(hourMinute);
    }

    public static Date getTimeNow() {
        Calendar calendar = Calendar.getInstance();
        try {
            return getTimeWithoutDate(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     *
     * @param time la fecha debe de ser el inicio de los tiempos, solo tomar la hora
     * @return
     */
    public static String getTimeWithoutDateStr(long time) {
        Log.i(TAG, "getTimeWithoutDateStr:" + time);
        long hours = TimeUnit.MILLISECONDS.toHours(time);//%24
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Date getTimeWithoutDate(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(HH_MM, Locale.getDefault());
        return sdf.parse(time);
    }

    public static int getAmOrPm(int hour) {
        return hour < 12 ? Calendar.AM : Calendar.PM;
    }

    public static boolean validateTime(final String time) {
        return validate(TIME24HOURS_PATTERN, time);
    }

    private static boolean validate(String pattern, String input) {
        return Pattern.compile(pattern).matcher(input).matches();
    }

    /**
     *
     * @param date
     * @param hour
     * @param min
     * @return
     */
    public static long getTimeMillis(String date, String hour, String min) {
        Calendar c = Calendar.getInstance();
        String[] split = date.split("-");
        c.set(Integer.parseInt(split[0]), Integer.parseInt(split[1])-1, Integer.parseInt(split[2]), Integer.parseInt(hour), Integer.parseInt(min));
        return c.getTimeInMillis();
    }

//    public static Date getDate(LocalDate today) {//import java.time.LocalDate;
//        return java.sql.Date.valueOf(today);
//    }
//
//    public static String getDateStr(LocalDate today) {
//        return getDateStr(getDate(today));
//    }
//
//    public static Date plusDays(Date date, int days) {
//        LocalDate ld = convertToLocalDate(date);
//        return getDate(ld.plusDays(days));
////		return getDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(days));
//    }
//
//    public static int getDayOfWeek(String dateStr) {
//        LocalDate ld = convertToLocalDate(dateStr);
//        return ld.getDayOfWeek().getValue();
//    }
//
//    private static LocalDate convertToLocalDate(Date date) {
//        return new java.sql.Date(date.getTime()).toLocalDate();
//    }
//
//    private static LocalDate convertToLocalDate(String dateStr) {
//        return LocalDate.parse(dateStr);
//    }

    public static int getDayOfWeek() {
        return LocalDate.now().getDayOfWeek();
    }

    public static int getDayOfWeek(Date date) {
        return LocalDate.fromDateFields(date).getDayOfWeek();
    }



}
