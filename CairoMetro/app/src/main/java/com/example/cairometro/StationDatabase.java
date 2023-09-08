package com.example.cairometro;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Station.class}, version = 1)
public abstract class StationDatabase extends RoomDatabase {
    public abstract StationDAO getStationDAO();
    private static StationDatabase ourInstance;
    public static StationDatabase getInstance(Context context){
        if(ourInstance == null){
            ourInstance = Room.databaseBuilder(context,
                            StationDatabase.class, "stations1.db")
                    .createFromAsset("databases/stations1.db")
                    .allowMainThreadQueries()
                    //.fallbackToDestructiveMigration()
                    .build();
        }

        return ourInstance;
    }
}
