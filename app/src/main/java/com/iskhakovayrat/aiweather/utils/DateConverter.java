package com.iskhakovayrat.aiweather.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    public static String getHoursAndMinutes(long dateLong) {
        SimpleDateFormat hoursFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(dateLong * 1000);
        return hoursFormat.format(date);
    }

    public static String getDayOfWeekAndHours(long dateLong){
        SimpleDateFormat hoursFormat = new SimpleDateFormat("E HH");
        Date date = new Date(dateLong * 1000);
        return hoursFormat.format(date) + "h";
    }

    public static String getDayOfWeek(long dateLong){
        SimpleDateFormat hoursFormat = new SimpleDateFormat("EEEE");
        Date date = new Date(dateLong * 1000);
        return hoursFormat.format(date);
    }
}
