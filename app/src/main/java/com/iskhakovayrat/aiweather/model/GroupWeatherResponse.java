package com.iskhakovayrat.aiweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupWeatherResponse {

    @SerializedName("list")
    private List<CurrentWeatherResponse> currentWeatherResponseList;

    public List<CurrentWeatherResponse> getCurrentWeatherResponseList() {
        return currentWeatherResponseList;
    }

    public void setCurrentWeatherResponseList(List<CurrentWeatherResponse> currentWeatherResponseList) {
        this.currentWeatherResponseList = currentWeatherResponseList;
    }
}
