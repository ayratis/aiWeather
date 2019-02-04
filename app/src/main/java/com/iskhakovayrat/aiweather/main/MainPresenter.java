package com.iskhakovayrat.aiweather.main;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.iskhakovayrat.aiweather.ConstantInterface;
import com.iskhakovayrat.aiweather.R;
import com.iskhakovayrat.aiweather.data.AppDatabase;
import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;
import com.iskhakovayrat.aiweather.model.DailyForecastParams;
import com.iskhakovayrat.aiweather.model.ThreeHoursForecastResponse;
import com.iskhakovayrat.aiweather.utils.DateConverter;
import com.iskhakovayrat.aiweather.utils.TempConverter;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MainPresenter {

    private int cityId;

    private MainModel model;
    private MainView view;
    private Context context;

    private CompositeDisposable disposables;

    public MainPresenter(Context context, MainModel mainModel) {
        model = mainModel;
        this.context = context;
        disposables = new CompositeDisposable();
    }

    public void attach(MainView view) {
        this.view = view;
        cityId = model.getLastCityId();
        showCashedWeatherData();
        loadCurrentWeather(cityId);
        loadThreeHoursForecast(cityId);
    }

    public void detach() {
        view = null;
        disposables.dispose();
    }

    private void showCashedWeatherData(){
        CurrentWeatherResponse currentWeatherResponse = model.getCashedCurrentWeather();
        if(currentWeatherResponse != null) {
            showCurrentWeather(currentWeatherResponse);
        }

        ThreeHoursForecastResponse threeHoursForecastResponse = model.getCashedForecastWeather();
        if(threeHoursForecastResponse != null){
            showForecast(threeHoursForecastResponse);
        } else {

        }
    }


    private void loadCurrentWeather(int cityId) {
        disposables
                .add(model.loadCurrentWeather(cityId)
                        .subscribe(this::showCurrentWeather, ignoreError -> {
                        }));
    }

    private void loadThreeHoursForecast(int cityId) {
        disposables
                .add(model.loadThreeHoursForecast(cityId)
                        .subscribe(this::showForecast, ignoreError -> {
                        }));
    }

    private void showCurrentWeather(CurrentWeatherResponse currentWeather) {

        String cityAndTemp = currentWeather.getName()
                + " " + TempConverter.convert(currentWeather.getMain().getTemp());
        view.showCurrentCityAndTemp(cityAndTemp);

        view.showCurrentWeather(currentWeather.getWeather().get(0).getMain());

        view.showCurrentIcon(ConstantInterface.iconUrlPath
                + currentWeather.getWeather().get(0).getIcon() + ".png");

        String mainDescription = context.getString(R.string.today) + ": "
                + currentWeather.getWeather().get(0).getDescription()
                + ", " + context.getString(R.string.sunrise) + " at "
                + DateConverter.getHoursAndMinutes(currentWeather.getSys().getSunrise())
                + ", " + context.getString(R.string.sunset) + " at "
                + DateConverter.getHoursAndMinutes(currentWeather.getSys().getSunset())
                + ".";
        view.showMainDescription(mainDescription);

        String mainPressure = context.getString(R.string.pressure) + ": "
                + currentWeather.getMain().getPressure()
                + " hPa";
        view.showMainPressure(mainPressure);

        String mainHumidity = context.getString(R.string.humidity) + ": "
                + currentWeather.getMain().getHumidity()
                + " %";
        view.showMainHumidity(mainHumidity);

        String mainWind = context.getString(R.string.wind_speed) + ": "
                + currentWeather.getWind().getSpeed()
                + " m/s";
        view.showMainWind(mainWind);

        String mainClouds = context.getString(R.string.clouds) + ": "
                + currentWeather.getClouds().getValue()
                + " %";
        view.showMainClouds(mainClouds);
    }

    private void showForecast(ThreeHoursForecastResponse threeHoursForecastResponse) {

        view.showThreeHoursForecast(threeHoursForecastResponse);

        List<DailyForecastParams> dailyForecastParamsList =
                model.getDailyForecastItems(threeHoursForecastResponse.getList());

        showDailyForecast(dailyForecastParamsList);
    }

    private void showDailyForecast(List<DailyForecastParams> items) {

        view.showDailyForecastDayOne(DateConverter.getDayOfWeek(items.get(1).getDate()),
                ConstantInterface.iconUrlPath + items.get(1).getWeatherIcon() + ".png",
                TempConverter.convert(items.get(1).getTempMin()),
                TempConverter.convert(items.get(1).getTempMax()));

        view.showDailyForecastDayTwo(DateConverter.getDayOfWeek(items.get(2).getDate()),
                ConstantInterface.iconUrlPath + items.get(2).getWeatherIcon() + ".png",
                TempConverter.convert(items.get(2).getTempMin()),
                TempConverter.convert(items.get(2).getTempMax()));

        view.showDailyForecastDayThree(DateConverter.getDayOfWeek(items.get(3).getDate()),
                ConstantInterface.iconUrlPath + items.get(3).getWeatherIcon() + ".png",
                TempConverter.convert(items.get(3).getTempMin()),
                TempConverter.convert(items.get(3).getTempMax()));

        view.showDailyForecastDayFour(DateConverter.getDayOfWeek(items.get(4).getDate()),
                ConstantInterface.iconUrlPath + items.get(4).getWeatherIcon() + ".png",
                TempConverter.convert(items.get(4).getTempMin()),
                TempConverter.convert(items.get(4).getTempMax()));

        view.showDailyForecastDayFive(DateConverter.getDayOfWeek(items.get(5).getDate()),
                ConstantInterface.iconUrlPath + items.get(5).getWeatherIcon() + ".png",
                TempConverter.convert(items.get(5).getTempMin()),
                TempConverter.convert(items.get(5).getTempMax()));
    }

    public void changeCity(int cityId) {
        model.saveLastCityId(cityId);
        view.clearData();
        showCashedWeatherData();
        loadCurrentWeather(cityId);
        loadThreeHoursForecast(cityId);
    }

}
