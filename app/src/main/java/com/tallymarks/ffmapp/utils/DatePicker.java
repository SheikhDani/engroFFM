package com.tallymarks.ffmapp.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;




public class DatePicker {

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Calendar myCalendar;
    private DatePickerDialog datePickerDialog;

    public void showDatePickerDialog(Context context, boolean disableBackDate, final EditText editText, final String dateFormat, final String selectedDate, String minDate, String maxDate) {
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editText.setText(selectedDate(dateFormat));
            }
        };
        if (selectedDate!=null) {
            if(!(selectedDate.equals(""))) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
                    Date date = sdf.parse(selectedDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    datePickerDialog=new DatePickerDialog(context, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                    if(disableBackDate) {
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    }
                    if(minDate!=null && maxDate!=null)
                    {
                        datePickerDialog.getDatePicker().setMinDate(convertDateToMilliSeconds(minDate));
                        datePickerDialog.getDatePicker().setMaxDate(convertDateToMilliSeconds(maxDate));
                    }
                    datePickerDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                datePickerDialog=new DatePickerDialog(context, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                if(disableBackDate) {
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                }
                if(minDate!=null && maxDate!=null)
                {
                    datePickerDialog.getDatePicker().setMinDate(convertDateToMilliSeconds(minDate));
                    datePickerDialog.getDatePicker().setMaxDate(convertDateToMilliSeconds(maxDate));
                }
                datePickerDialog.show();
            }
        }
        else {
            datePickerDialog=new DatePickerDialog(context, dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            if(disableBackDate) {
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
            if(minDate!=null && maxDate!=null)
            {
                datePickerDialog.getDatePicker().setMinDate(convertDateToMilliSeconds(minDate));
                datePickerDialog.getDatePicker().setMaxDate(convertDateToMilliSeconds(maxDate));
            }
            datePickerDialog.show();
        }
    }


    private String selectedDate(String dateFormat)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return sdf.format(myCalendar.getTime());
    }


    public float getDaysBetweenDates(String startDate, String endDate, String dateFormat)
    {
        float noOfDays=0;
        SimpleDateFormat myFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        try
        {
            Date dateBefore = myFormat.parse(startDate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            noOfDays = (difference / (1000*60*60*24))+1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfDays;
    }

    public static String getNextDate(String currentDate)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        Date date;
        Calendar calendar = null;
        try {
            date = format.parse(currentDate);
            calendar= Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(calendar.getTime());
    }

    public static String getFirstDateOfMonth()
    {
        String firstDate="";
        try
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            firstDate=Helpers.utcToAnyDateFormat(String.valueOf(cal.getTime()), Constants.GMT_TIME_FORMAT, Constants.DATE_FORMAT);
        }
        catch (Exception e)
        {
            Log.e("Exception : ",e.getMessage());
        }
        return firstDate;
    }

    public static String getLastDateOfMonth()
    {
        String lastDate="";
        try
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            lastDate=Helpers.utcToAnyDateFormat(String.valueOf(cal.getTime()), Constants.GMT_TIME_FORMAT, Constants.DATE_FORMAT);
        }
        catch (Exception e)
        {
            Log.e("Exception : ",e.getMessage());
        }
        return lastDate;
    }

    private long convertDateToMilliSeconds(String date)
    {
        long milliseconds=0;
        SimpleDateFormat f = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            Date d = f.parse(date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }
}
