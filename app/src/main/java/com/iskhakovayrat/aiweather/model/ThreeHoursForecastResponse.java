package com.iskhakovayrat.aiweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThreeHoursForecastResponse {

    @SerializedName("list")
    private List<ThreeHoursForecastListItem> list;

    @SerializedName("city")
    private City city;

    public List<ThreeHoursForecastListItem> getList() {
        return list;
    }

    public void setList(List<ThreeHoursForecastListItem> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
