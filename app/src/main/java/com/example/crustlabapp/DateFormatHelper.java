package com.example.crustlabapp;

import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatHelper {

    private static final java.text.DateFormat DateAndTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final java.text.DateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String getActualDateAndTime() {
        String dateString;
        dateString = DateAndTimeFormat.format(Calendar.getInstance().getTime());
        return dateString;
    }

    public static String getActualDate() {
        String dateString;
        dateString = DateFormat.format(Calendar.getInstance().getTime());
        return dateString;
    }

    public static Date getDateAndTimeOfStringDate(String dateString) {
        Date date = null;
        try {
            date = DateAndTimeFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getStringDateOfDate(Date date) {
        return DateFormat.format(date);
    }

    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
}
