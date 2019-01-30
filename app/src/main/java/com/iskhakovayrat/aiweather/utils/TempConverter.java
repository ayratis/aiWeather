package com.iskhakovayrat.aiweather.utils;

public class TempConverter {

    public static String convert(double temp){
        double calvin = 273.15;
        temp = temp - calvin;
        int celTemp = (int) Math.round(temp);
        return "" + celTemp + "°";
    }

}
