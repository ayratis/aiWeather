package com.iskhakovayrat.aiweather.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CityData {

    @PrimaryKey
    public int cityId;

    @ColumnInfo
    public String cityName;

}
