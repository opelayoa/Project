package com.tiendas3b.almacen.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.tiendas3b.almacen.views.widgets.RangeTimePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by dfa on 04/07/2016.
 */
public class DialogUtil {

    public static void showCalendar(Context context, Calendar c, DatePickerDialog.OnDateSetListener listener) {
        initDialog(context, c, listener).show();
    }

    public static void showCalendarTodayMax(Context context, Calendar c, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dateDialog = initDialog(context, c, listener);
        dateDialog.getDatePicker().setMaxDate(new Date().getTime());
        dateDialog.show();
    }

    public static void showCalendarTodayMim(Context context, Calendar c, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dateDialog = initDialog(context, c, listener);
        dateDialog.getDatePicker().setMinDate(new Date().getTime());
        dateDialog.show();
    }

    public static void showCalendarTodayPlusDaysMim(Context context, Calendar c, int extraDays, DatePickerDialog.OnDateSetListener listener) {
        long extraDaysMillis = TimeUnit.MILLISECONDS.convert(extraDays, TimeUnit.DAYS);
        DatePickerDialog dateDialog = initDialog(context, c, listener);
        dateDialog.getDatePicker().setMinDate(new Date().getTime() + extraDaysMillis);
        dateDialog.show();
    }

    private static DatePickerDialog initDialog(Context context, Calendar c, DatePickerDialog.OnDateSetListener listener) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(context, listener, year, month, day);
    }

    public static void showClock(Context context, Calendar c, TimePickerDialog.OnTimeSetListener listener, int tittle) {
        initClockDialog(context, c, listener, tittle).show();
    }

    private static TimePickerDialog initClockDialog(Context context, Calendar c, TimePickerDialog.OnTimeSetListener listener, int tittle) {
        int am = c.get(Calendar.AM_PM);
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog d = new TimePickerDialog(context, listener, am == 0 ? hour : hour + 12, minute, true);
        d.setTitle(tittle);
        return d;
    }

    public static void showAlertDialog(final Context context, int message, DialogInterface.OnClickListener listener){
        showAlertDialog(context, context.getString(message), context.getString(android.R.string.ok), context.getString(android.R.string.cancel), listener, null);
    }

    public static void showAlertDialog(final Context context, String customMessage, String positiveText, String negativeText, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener negativeListener){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(customMessage);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(positiveText, listener);
        alertDialogBuilder.setNegativeButton(negativeText, negativeListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, int message) {
        showAlertDialog(context, context.getString(message), context.getString(android.R.string.ok), context.getString(android.R.string.cancel), null, null);//TODO verificar que no agrega boton cancelar
    }

    public static void showRangeClock(final Context context, TimePickerDialog.OnTimeSetListener listener, int tittle, int minHour, int minMinute, int maxHour, int maxMinute) {
//        KeyboardUtil.hide(context);
        initRangeClockDialog(context, listener, tittle, minHour, minMinute, maxHour, maxMinute).show();
    }

    private static RangeTimePickerDialog initRangeClockDialog(Context context, TimePickerDialog.OnTimeSetListener listener, int tittle, int minHour, int minMinute, int maxHour, int maxMinute) {
        RangeTimePickerDialog d = new RangeTimePickerDialog(context, listener, /*isAm == Calendar.AM ? hour : hour + 12*/minHour, minMinute, true);
        d.setTitle(tittle);
        d.setMin(minHour, minMinute);
        d.setMax(maxHour, maxMinute);
        return d;
    }

    public static void showRangeClock(Context context, TimePickerDialog.OnTimeSetListener listener, int tittle, int minHour, int minMinute) {
        initRangeClockDialog(context, listener, tittle, minHour, minMinute, 23, 59).show();
    }

    public static void showAlertDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        showAlertDialog(context, message, context.getString(android.R.string.ok), listener);//TODO verificar que no agrega boton cancelar
    }

    private static void showAlertDialog(Context context, String message, String positiveText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(positiveText, listener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showCheckBoxes(Context context, String tittle, CharSequence[] paybills, boolean[] selectedItemsArr, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener,
                                      DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener1) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(tittle)
                .setMultiChoiceItems(paybills, selectedItemsArr, onMultiChoiceClickListener)
                .setPositiveButton("Ok", onClickListener)
                .setNegativeButton("Cancel", onClickListener1).create();
        dialog.show();
    }
}
