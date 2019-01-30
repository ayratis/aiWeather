package com.iskhakovayrat.aiweather.city_list;

import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;

import java.util.List;

public interface CityListView {

    void showListInfo(List<CurrentWeatherResponse> items);

    void addListItem(CurrentWeatherResponse currentWeatherResponse);

    void deleteItem(int cityId);

    void showToastWrongCity();

    void showToastAlreadyOn();

    void showToastTooMuch();
}
