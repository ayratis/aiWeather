package com.iskhakovayrat.aiweather.city_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iskhakovayrat.aiweather.Api;
import com.iskhakovayrat.aiweather.R;
import com.iskhakovayrat.aiweather.data.AppDatabase;
import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class CityListActivity extends AppCompatActivity
        implements CityListView, CityDialogFragment.ChooseCityDialogListener {

    private RecyclerView cityListRecyclerView;
    private Button cityListAddButton;

    private CityListPresenter presenter;

    private CityListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        cityListRecyclerView = findViewById(R.id.cityListRecyclerView);

        cityListAddButton = findViewById(R.id.cityListAddCityListButton);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Api.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        presenter = new CityListPresenter(new CityListModel(AppDatabase.getInstance(this), new Gson(), api));
        presenter.attach(this);

        cityListAddButton.setOnClickListener(v -> {
            DialogFragment newFragment = new CityDialogFragment();
            newFragment.show(getSupportFragmentManager(), "choose_city");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void showListInfo(List<CurrentWeatherResponse> items) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new CityListAdapter(
                items,
                id -> presenter.deleteCity(id),
                cityId -> {
                    Intent intent = new Intent();
                    intent.putExtra("cityId", cityId);
                    setResult(RESULT_OK, intent);
                    finish();
                });
        cityListRecyclerView.setLayoutManager(layoutManager);
        cityListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void addListItem(CurrentWeatherResponse currentWeatherResponse) {
        adapter.addItem(currentWeatherResponse);
    }

    @Override
    public void deleteItem(int cityId) {
        adapter.deleteItem(cityId);
    }

    @Override
    public void showToastWrongCity() {
        Toast.makeText(this, getString(R.string.wrong_city_name), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastAlreadyOn() {
        Toast.makeText(this, getString(R.string.already_on_list), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastTooMuch() {
        Toast.makeText(this, getString(R.string.too_much_cities), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String cityName) {
        presenter.loadCurrentWeatherInfo(cityName);
    }
}
