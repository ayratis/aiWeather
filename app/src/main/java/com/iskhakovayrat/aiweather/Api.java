package com.iskhakovayrat.aiweather;

import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;
import com.iskhakovayrat.aiweather.model.GroupWeatherResponse;
import com.iskhakovayrat.aiweather.model.ThreeHoursForecastResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "http://api.openweathermap.org/";
    String VERSION = "data/2.5/";
    String APPID = "8c411b9e8f1da5a3bdc024c91c490639";

    @GET(VERSION + "weather")
    Observable<CurrentWeatherResponse> loadCurrentWeather(@Query("id") int cityId,
                                                          @Query("appid") String appid);

    @GET(VERSION + "weather")
    Observable<CurrentWeatherResponse> loadCurrentWeatherByCityName(@Query("q") String cityName,
                                                                    @Query("appid") String appid);

    @GET(VERSION + "forecast")
    Observable<ThreeHoursForecastResponse> loadThreeHoursForecast(@Query("id") int cityId,
                                                                  @Query("appid") String appid);

    @GET(VERSION + "group")
    Observable<GroupWeatherResponse> loadCurrentWeatherGroup(@Query("id") String ids,
                                                             @Query("appid") String appid);
}
