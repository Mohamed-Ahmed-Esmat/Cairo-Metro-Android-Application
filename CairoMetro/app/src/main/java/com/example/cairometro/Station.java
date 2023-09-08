package com.example.cairometro;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Stations")
public class Station {
    @PrimaryKey(autoGenerate = true)
    public int stationID;
    public String EnglishName, ArabicName, Line;
    public float Latitude, Longitude;
}
