package com.iskhakovayrat.aiweather.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CityDataDao {
    @Query("SELECT * FROM cityData")
    List<CityData> getAll();

    @Query("SELECT Count(*) FROM cityData")
    int getItemCount();

    @Query("SELECT * FROM cityData WHERE cityId IN (:cityIds)")
    List<CityData> loadAllByIds(int[] cityIds);

    @Query("SELECT * FROM citydata WHERE cityName LIKE :cityName LIMIT 1")
    CityData findByCityName(String cityName);

    @Query("SELECT * FROM citydata WHERE cityId LIKE :cityId LIMIT 1")
    CityData findByCityId(int cityId);

    @Insert
    void insertAll(CityData... cityData);

    @Delete
    void delete(CityData cityData);

}
