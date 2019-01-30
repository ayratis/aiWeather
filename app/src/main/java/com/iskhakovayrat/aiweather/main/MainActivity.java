package com.iskhakovayrat.aiweather.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iskhakovayrat.aiweather.ConstantInterface;
import com.iskhakovayrat.aiweather.R;
import com.iskhakovayrat.aiweather.city_list.CityListActivity;
import com.iskhakovayrat.aiweather.model.ThreeHoursForecastResponse;

public class MainActivity extends AppCompatActivity implements MainView {

    public static final int GET_CITYID_REQUEST_CODE = 1;

    private TextView mainCityNameAndTemp;
    private TextView mainWeather;
    private ImageView mainIcon;

    private RecyclerView mainThreeHoursForecast;

    private TextView mainForecastNameDayOne;
    private ImageView mainForecastIconDayOne;
    private TextView mainForecastTempMinDayOne;
    private TextView mainForecastTempMaxDayOne;

    private TextView mainForecastNameDayTwo;
    private ImageView mainForecastIconDayTwo;
    private TextView mainForecastTempMinDayTwo;
    private TextView mainForecastTempMaxDayTwo;

    private TextView mainForecastNameDayThree;
    private ImageView mainForecastIconDayThree;
    private TextView mainForecastTempMinDayThree;
    private TextView mainForecastTempMaxDayThree;

    private TextView mainForecastNameDayFour;
    private ImageView mainForecastIconDayFour;
    private TextView mainForecastTempMinDayFour;
    private TextView mainForecastTempMaxDayFour;

    private TextView mainForecastNameDayFive;
    private ImageView mainForecastIconDayFive;
    private TextView mainForecastTempMinDayFive;
    private TextView mainForecastTempMaxDayFive;

    private TextView mainDescription;
    private TextView mainPressure;
    private TextView mainHumidity;
    private TextView mainWind;
    private TextView mainClouds;

    private Button mainGoToCityListButton;

    private MainPresenter presenter;

    private ThreeHoursForecastAdapter threeHoursForecastAdapter;

    private int cityId = ConstantInterface.KAZAN_ID;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case GET_CITYID_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    cityId = data.getIntExtra("cityId", -1);
                }
                presenter.changeCity(cityId);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainCityNameAndTemp = findViewById(R.id.mainCityNameAndTemp);
        mainWeather = findViewById(R.id.mainWeather);
        mainIcon = findViewById(R.id.mainIcon);

        mainThreeHoursForecast = findViewById(R.id.mainThreeHoursForecast);

        mainForecastNameDayOne = findViewById(R.id.mainForecastNameDayOne);
        mainForecastIconDayOne = findViewById(R.id.mainForecastIconDayOne);
        mainForecastTempMinDayOne = findViewById(R.id.mainForecastTempMinDayOne);
        mainForecastTempMaxDayOne = findViewById(R.id.mainForecastTempMaxDayOne);

        mainForecastNameDayTwo = findViewById(R.id.mainForecastNameDayTwo);
        mainForecastIconDayTwo = findViewById(R.id.mainForecastIconDayTwo);
        mainForecastTempMinDayTwo = findViewById(R.id.mainForecastTempMinDayTwo);
        mainForecastTempMaxDayTwo = findViewById(R.id.mainForecastTempMaxDayTwo);

        mainForecastNameDayThree = findViewById(R.id.mainForecastNameDayThree);
        mainForecastIconDayThree = findViewById(R.id.mainForecastIconDayThree);
        mainForecastTempMinDayThree = findViewById(R.id.mainForecastTempMinDayThree);
        mainForecastTempMaxDayThree = findViewById(R.id.mainForecastTempMaxDayThree);

        mainForecastNameDayFour = findViewById(R.id.mainForecastNameDayFour);
        mainForecastIconDayFour = findViewById(R.id.mainForecastIconDayFour);
        mainForecastTempMinDayFour = findViewById(R.id.mainForecastTempMinDayFour);
        mainForecastTempMaxDayFour = findViewById(R.id.mainForecastTempMaxDayFour);

        mainForecastNameDayFive = findViewById(R.id.mainForecastNameDayFive);
        mainForecastIconDayFive = findViewById(R.id.mainForecastIconDayFive);
        mainForecastTempMinDayFive = findViewById(R.id.mainForecastTempMinDayFive);
        mainForecastTempMaxDayFive = findViewById(R.id.mainForecastTempMaxDayFive);

        mainDescription = findViewById(R.id.mainDescription);
        mainPressure = findViewById(R.id.mainPressure);
        mainHumidity = findViewById(R.id.mainHumidity);
        mainWind = findViewById(R.id.mainWind);
        mainClouds = findViewById(R.id.mainClouds);

        mainGoToCityListButton = findViewById(R.id.mainGoToCityListButton);
        mainGoToCityListButton.setOnClickListener(v -> startActivityForResult(
                new Intent(this, CityListActivity.class),
                GET_CITYID_REQUEST_CODE));

        presenter = new MainPresenter(this);
        presenter.attach(this, cityId);

    }

    @Override
    public void showCurrentCityAndTemp(String cityAndTemp) {
        mainCityNameAndTemp.setText(cityAndTemp);
    }

    @Override
    public void showCurrentWeather(String weather) {
        mainWeather.setText(weather);
    }

    @Override
    public void showCurrentIcon(String iconUrl) {
        Glide.with(this).load(iconUrl).into(mainIcon);
    }

    @Override
    public void showThreeHoursForecast(ThreeHoursForecastResponse threeHoursForecastResponse) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        threeHoursForecastAdapter = new ThreeHoursForecastAdapter(threeHoursForecastResponse);
        mainThreeHoursForecast.setLayoutManager(layoutManager);
        mainThreeHoursForecast.setAdapter(threeHoursForecastAdapter);
    }

    @Override
    public void showDailyForecastDayOne(String day, String iconUrl, String minTemp, String maxTemp) {
        mainForecastNameDayOne.setText(day);
        Glide.with(this).load(iconUrl).into(mainForecastIconDayOne);
        mainForecastTempMinDayOne.setText(minTemp);
        mainForecastTempMaxDayOne.setText(maxTemp);
    }

    @Override
    public void showDailyForecastDayTwo(String day, String iconUrl, String minTemp, String maxTemp) {
        mainForecastNameDayTwo.setText(day);
        Glide.with(this).load(iconUrl).into(mainForecastIconDayTwo);
        mainForecastTempMinDayTwo.setText(minTemp);
        mainForecastTempMaxDayTwo.setText(maxTemp);
    }

    @Override
    public void showDailyForecastDayThree(String day, String iconUrl, String minTemp, String maxTemp) {
        mainForecastNameDayThree.setText(day);
        Glide.with(this).load(iconUrl).into(mainForecastIconDayThree);
        mainForecastTempMinDayThree.setText(minTemp);
        mainForecastTempMaxDayThree.setText(maxTemp);
    }

    @Override
    public void showDailyForecastDayFour(String day, String iconUrl, String minTemp, String maxTemp) {
        mainForecastNameDayFour.setText(day);
        Glide.with(this).load(iconUrl).into(mainForecastIconDayFour);
        mainForecastTempMinDayFour.setText(minTemp);
        mainForecastTempMaxDayFour.setText(maxTemp);
    }

    @Override
    public void showDailyForecastDayFive(String day, String iconUrl, String minTemp, String maxTemp) {
        mainForecastNameDayFive.setText(day);
        Glide.with(this).load(iconUrl).into(mainForecastIconDayFive);
        mainForecastTempMinDayFive.setText(minTemp);
        mainForecastTempMaxDayFive.setText(maxTemp);
    }

    @Override
    public void showMainDescription(String description) {
        mainDescription.setText(description);
    }

    @Override
    public void showMainPressure(String pressure) {
        mainPressure.setText(pressure);
    }

    @Override
    public void showMainHumidity(String humidity) {
        mainHumidity.setText(humidity);
    }

    @Override
    public void showMainWind(String wind) {
        mainWind.setText(wind);
    }

    @Override
    public void showMainClouds(String clouds) {
        mainClouds.setText(clouds);
    }
}
