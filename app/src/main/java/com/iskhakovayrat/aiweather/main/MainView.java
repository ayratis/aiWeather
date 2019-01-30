package com.iskhakovayrat.aiweather.main;

import com.iskhakovayrat.aiweather.model.ThreeHoursForecastResponse;

public interface MainView {
    void showCurrentCityAndTemp(String cityAndTemp);

    void showCurrentWeather(String weather);

    void showCurrentIcon(String iconUrl);

    void showThreeHoursForecast(ThreeHoursForecastResponse threeHoursForecastResponse);

    void showDailyForecastDayOne(String day, String iconUrl, String minTemp, String maxTemp);

    void showDailyForecastDayTwo(String day, String iconUrl, String minTemp, String maxTemp);

    void showDailyForecastDayThree(String day, String iconUrl, String minTemp, String maxTemp);

    void showDailyForecastDayFour(String day, String iconUrl, String minTemp, String maxTemp);

    void showDailyForecastDayFive(String day, String iconUrl, String minTemp, String maxTemp);

    void showMainDescription(String description);

    void showMainPressure(String pressure);

    void showMainHumidity(String humidity);

    void showMainWind(String wind);

    void showMainClouds(String clouds);

}
