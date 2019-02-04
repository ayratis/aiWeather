package com.iskhakovayrat.aiweather.main;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.iskhakovayrat.aiweather.Api;
import com.iskhakovayrat.aiweather.ConstantInterface;
import com.iskhakovayrat.aiweather.data.AppDatabase;
import com.iskhakovayrat.aiweather.data.CityData;
import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;
import com.iskhakovayrat.aiweather.model.DailyForecastParams;
import com.iskhakovayrat.aiweather.model.ThreeHoursForecastListItem;
import com.iskhakovayrat.aiweather.model.ThreeHoursForecastResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainModel {

    private static final String KEY_LAST_CITY_ID = "keyLastCityId";

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private Api api;
    private AppDatabase db;
    private Gson gson;
    private SharedPreferences prefs;

    public MainModel(AppDatabase db, Gson gson, SharedPreferences prefs, Api api) {
        this.db = db;
        this.gson = gson;
        this.prefs = prefs;
        this.api = api;
    }

    public Observable<CurrentWeatherResponse> loadCurrentWeather(int cityId) {
        return api.loadCurrentWeather(cityId, Api.APPID)
                .map(currentWeatherResponse -> {
                    saveCurrentWeather(currentWeatherResponse);
                    return currentWeatherResponse;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThreeHoursForecastResponse> loadThreeHoursForecast(int cityId) {
        return api.loadThreeHoursForecast(cityId, Api.APPID)
                .map(threeHoursForecastResponse -> {
                    saveForecastWeather(threeHoursForecastResponse);
                    return threeHoursForecastResponse;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public List<DailyForecastParams> getDailyForecastItems(List<ThreeHoursForecastListItem> items) {

        List<DailyForecastParams> result = new ArrayList<>();

        int nextDayPosition = getNextDayPosition(items);

        result.add(getDayItem(items, 0, nextDayPosition));   //0-nextDayPosition uses for current day info
        result.add(getDayItem(items, nextDayPosition, nextDayPosition + 8));
        result.add(getDayItem(items, nextDayPosition + 8, nextDayPosition + 16));
        result.add(getDayItem(items, nextDayPosition + 16, nextDayPosition + 24));
        result.add(getDayItem(items, nextDayPosition + 24, nextDayPosition + 32));
        result.add(getDayItem(items, nextDayPosition + 32, items.size()));

        return result;
    }

    private DailyForecastParams getDayItem(List<ThreeHoursForecastListItem> items,
                                           int start, int end) {

        List<String> weatherIconList = new ArrayList<>();

        double tempMin = items.get(start).getMain().getTempMin();
        double tempMax = items.get(start).getMain().getTempMax();

        for (int i = start; i < end; i++) {

            if (items.get(i).getWeather().get(0).getIcon().contains("d")) {   //collecting only day icons
                weatherIconList.add(items.get(i).getWeather().get(0).getIcon());
            }
            if (items.get(i).getMain().getTempMax() > tempMax) {
                tempMax = items.get(i).getMain().getTempMax();
            }
            if (items.get(i).getMain().getTempMin() < tempMin) {
                tempMin = items.get(i).getMain().getTempMin();
            }
        }

        String weatherIcon;

        if (weatherIconList.isEmpty()) {
            weatherIcon = items.get(end - 1).getWeather().get(0).getIcon();   //if we don't have fifth day's d-icon
        } else {
            weatherIcon = getFrequentWeatherIcon(weatherIconList);
        }

        long date = items.get(start).getDt();

        return new DailyForecastParams(tempMin, tempMax, weatherIcon, date);
    }

    private int getNextDayPosition(List<ThreeHoursForecastListItem> items) {
        for (int i = 1; i < items.size(); i++) {
            if (!items.get(i).getDtTxt().substring(8, 10)
                    .equals(items.get(i - 1).getDtTxt().substring(8, 10))) {
                return i;
            }
        }
        return -1;
    }

    private String getFrequentWeatherIcon(List<String> itemList) {

        Collections.sort(itemList);

        String frequentElement = null;
        int maxCount = 0;
        int currentCount = 0;

        for (int i = 1; i < itemList.size(); i++) {
            if (currentCount >= maxCount) {
                frequentElement = itemList.get(i - 1);
            }
            if (itemList.get(i).equals(itemList.get(i - 1))) {
                currentCount++;
            } else {
                if (currentCount > maxCount) {
                    maxCount = currentCount;
                    frequentElement = itemList.get(i - 1);
                    currentCount = 0;
                } else {
                    currentCount = 0;
                }
            }
        }

        return frequentElement;
    }

    public int getLastCityId() {
        return prefs.getInt(KEY_LAST_CITY_ID, ConstantInterface.KAZAN_ID);
    }

    public void saveLastCityId(int cityId) {
        prefs.edit().putInt(KEY_LAST_CITY_ID, cityId).apply();
    }

    public CurrentWeatherResponse getCashedCurrentWeather() {
        CityData cityData = db.cityDataDao().findByCityId(getLastCityId());
        return cityData != null && cityData.currentWeather != null ?
                gson.fromJson(cityData.currentWeather, CurrentWeatherResponse.class) : null;

    }

    public ThreeHoursForecastResponse getCashedForecastWeather() {
        CityData cityData = db.cityDataDao().findByCityId(getLastCityId());
        return cityData != null && cityData.forecastWeather != null ?
                gson.fromJson(cityData.forecastWeather, ThreeHoursForecastResponse.class) : null;
    }

    private void saveCurrentWeather(CurrentWeatherResponse currentWeatherResponse) {
        CityData cityData = db.cityDataDao().findByCityId(currentWeatherResponse.getId());
        if (cityData != null) {
            cityData.currentWeather = gson.toJson(currentWeatherResponse);
        } else {
            cityData = new CityData();
            cityData.cityId = currentWeatherResponse.getId();
            cityData.cityName = currentWeatherResponse.getName();
            cityData.currentWeather = gson.toJson(currentWeatherResponse);
        }
        db.cityDataDao().insertAll(cityData);
    }

    private void saveForecastWeather(ThreeHoursForecastResponse threeHoursForecastResponse) {
        CityData cityData = db.cityDataDao().findByCityId(threeHoursForecastResponse.getCity().getId());
        if (cityData != null) {
            cityData.forecastWeather = gson.toJson(threeHoursForecastResponse);
        } else {
            cityData = new CityData();
            cityData.cityId = threeHoursForecastResponse.getCity().getId();
            cityData.cityName = threeHoursForecastResponse.getCity().getName();
            cityData.forecastWeather = gson.toJson(threeHoursForecastResponse);
        }
        db.cityDataDao().insertAll(cityData);
    }

}
