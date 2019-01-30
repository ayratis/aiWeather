package com.iskhakovayrat.aiweather.city_list;

import com.iskhakovayrat.aiweather.data.AppDatabase;
import com.iskhakovayrat.aiweather.model.CurrentWeatherResponse;

import io.reactivex.disposables.CompositeDisposable;

public class CityListPresenter {

    private CityListView view;
    private CityListModel model;

    private CompositeDisposable disposables;

    public CityListPresenter(AppDatabase db) {
        model = new CityListModel(db);
    }

    public void attach(CityListView view) {
        this.view = view;
        model.checkForMoscowKazan();
        disposables = new CompositeDisposable();
        loadGroupWeatherInfo(model.getIds());
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
            model.insertInDb(currentWeatherResponse.getId(),
                    currentWeatherResponse.getName());
            view.addListItem(currentWeatherResponse);
        }
    }

    public void deleteCity(int cityId) {
        model.deleteFromDb(cityId);
        view.deleteItem(cityId);
    }

}
