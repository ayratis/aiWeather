package com.iskhakovayrat.aiweather.city_list;

import com.iskhakovayrat.aiweather.Api;
import com.iskhakovayrat.aiweather.ConstantInterface;
import com.iskhakovayrat.aiweather.data.AppDatabase;
import com.iskhakovayrat.aiweather.data.CityData;
import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;
import com.iskhakovayrat.aiweather.model.GroupWeatherResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class CityListModel {

    private AppDatabase db;

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private Api api;

    public CityListModel(AppDatabase db) {
        this.db = db;

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

    public String getIds() {

        List<CityData> cityDataList = db.cityDataDao().getAll();
        String ids = "";

        for (int i = 0; i < cityDataList.size(); i++) {
            if (i == cityDataList.size() - 1) {
                ids = ids + cityDataList.get(i).cityId;
            } else {
                ids = ids + cityDataList.get(i).cityId + ",";
            }
        }

        return ids;
    }

    public void checkForMoscowKazan() {

        if (db.cityDataDao().findByCityName("Moscow") == null) {
            CityData cityMoscow = new CityData();
            cityMoscow.cityId = ConstantInterface.MOSCOW_ID;
            cityMoscow.cityName = "Moscow";
            db.cityDataDao().insertAll(cityMoscow);
        }
        if(db.cityDataDao().findByCityName("Kazan") == null){
            CityData cityKazan = new CityData();
            cityKazan.cityId = ConstantInterface.KAZAN_ID;
            cityKazan.cityName = "Kazan";
            db.cityDataDao().insertAll(cityKazan);
        }

    }

    public Observable<GroupWeatherResponse> loadGroupWeatherInfo(String ids) {
        return api.loadCurrentWeatherGroup(ids, Api.APPID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CurrentWeatherResponse> loadCurrentWeatherInfo(String cityName) {
        return api.loadCurrentWeatherByCityName(cityName, Api.APPID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public boolean isInDb(int id){
        return db.cityDataDao().findByCityId(id) != null;
    }

    public int getDbItemcount(){
        int i = db.cityDataDao().getItemCount();
        return i;
    }

    public void insertInDb(int id, String name){
        CityData cityData = new CityData();
        cityData.cityId = id;
        cityData.cityName = name;
        db.cityDataDao().insertAll(cityData);
    }

    public void deleteFromDb(int id){
        CityData cityData = db.cityDataDao().findByCityId(id);
        db.cityDataDao().delete(cityData);

    }

}
