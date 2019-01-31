package com.iskhakovayrat.aiweather.city_list;

import com.google.gson.Gson;
import com.iskhakovayrat.aiweather.data.AppDatabase;
import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;

import io.reactivex.disposables.CompositeDisposable;

public class CityListPresenter {

    private CityListView view;
    private CityListModel model;

    private CompositeDisposable disposables;

    public CityListPresenter(AppDatabase db, Gson gson) {
        model = new CityListModel(db, gson);
    }

    public void attach(CityListView view) {
        this.view = view;
        model.checkForMoscowKazan();
        disposables = new CompositeDisposable();
        showCashedGroupWeatherInfo();
        loadGroupWeatherInfo(model.getIds());
    }

    private void showCashedGroupWeatherInfo() {
        view.showListInfo(model.getCashedGroupWeatherData());
    }

    public void detach() {
        view = null;
        disposables.dispose();
    }

    private void loadGroupWeatherInfo(String ids) {
        disposables
                .add(model.loadGroupWeatherInfo(ids)
                        .subscribe(groupWeatherResponse -> view.showListInfo
                                        (groupWeatherResponse.getCurrentWeatherResponseList()),
                                error -> {}));
    }

    public void loadCurrentWeatherInfo(String cityName) {
        disposables
                .add(model.loadCurrentWeatherInfo(cityName)
                        .subscribe(this::addNewCity,
                                error -> view.showToastWrongCity()));
    }

    private void addNewCity(CurrentWeatherResponse currentWeatherResponse) {
        if (model.isInDb(currentWeatherResponse.getId())) {
            view.showToastAlreadyOn();
        } else if (model.getDbItemcount() >= 20) {
            view.showToastTooMuch();
        } else {
            model.saveCurrentWeatherData(currentWeatherResponse);
            view.addListItem(currentWeatherResponse);
        }
    }

    public void deleteCity(int cityId) {
        model.deleteFromDb(cityId);
        view.deleteItem(cityId);
    }

}
