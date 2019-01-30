package com.iskhakovayrat.aiweather.main;

import com.iskhakovayrat.aiweather.Api;
import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;
import com.iskhakovayrat.aiweather.model.ThreeHoursForecastListItem;
import com.iskhakovayrat.aiweather.model.ThreeHoursForecastResponse;
import com.iskhakovayrat.aiweather.model.DailyForecastParams;

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

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private Api api;

    public MainModel() {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Api.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);
    }

    public Observable<CurrentWeatherResponse> loadCurrentWeather(int cityId) {
        return api.loadCurrentWeather(cityId, Api.APPID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThreeHoursForecastResponse> loadThreeHoursForecast(int cityId) {
        return api.loadThreeHoursForecast(cityId, Api.APPID)
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

        if(weatherIconList.isEmpty()){
            weatherIcon = items.get(end-1).getWeather().get(0).getIcon();   //if we don't have fifth day's d-icon
        } else {
            weatherIcon = getFrequentWeatherIcon(weatherIconList);
        }

        long date = items.get(start).getDt();

        return new DailyForecastParams(tempMin, tempMax, weatherIcon, date);
    }

    private int getNextDayPosition(List<ThreeHoursForecastListItem> items) {
        for (int i = 1; i < items.size(); i++) {
            if (!items.get(i).getDtTxt().substring(8,10)
                    .equals(items.get(i - 1).getDtTxt().substring(8,10))) {
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


}
