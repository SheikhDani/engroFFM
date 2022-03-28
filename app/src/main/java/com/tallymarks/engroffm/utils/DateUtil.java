package com.tallymarks.engroffm.utils;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String getCurrentDateTime(Context context, String dateTimeFormat) {
        String currentDateTime = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
            currentDateTime = sdf.format(new Date());
        } catch (Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return currentDateTime;
    }

    public static String getCurrentDate(String format)
    {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(Calendar.getInstance().getTime());
    }

    public static String getPastDate(String inputDateFormat,int noOfDays)
    {
        DateFormat dateFormat = new SimpleDateFormat(inputDateFormat);
        Date myDate = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.DATE, -noOfDays);
        return dateFormat.format(cal.getTime());
    }

    public static String getFutureDate(String inputDateFormat,int noOfDays)
    {
        DateFormat dateFormat = new SimpleDateFormat(inputDateFormat);
        Date myDate = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.DATE, +noOfDays);
        return dateFormat.format(cal.getTime());
    }


}
