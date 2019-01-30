package com.iskhakovayrat.aiweather.model;

public class DailyForecastParams {

    private double tempMin;
    private double tempMax;
    private String weatherIcon;
    private long date;

    public DailyForecastParams(double tempMin, double tempMax, String weather, long date) {
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.weatherIcon = weather;
        this.date = date;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public long getDate() {
        return date;
    }
}
