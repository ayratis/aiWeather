package com.iskhakovayrat.aiweather.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {CityData.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase{

    public abstract CityDataDao cityDataDao();
}
