package com.iskhakovayrat.aiweather.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {CityData.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase{

    private static final String DB_NAME = "city_database";

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract CityDataDao cityDataDao();
}
