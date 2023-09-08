package com.example.cairometro;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface StationDAO {
    @Query("select EnglishName FROM Stations where Line = 'line 1'")
    List<String> line_1();
    @Query("select EnglishName FROM Stations where Line = 'line 2'")
    List<String> line_2();
    @Query("select EnglishName FROM Stations where Line = 'line 3'")
    List<String> line_3();
    @Query("select DISTINCT EnglishName FROM Stations")
    List<String> lines_all();
    @Query("select Latitude from( SELECT DISTINCT EnglishName, Latitude FROM Stations)")
    List<Float> lats();
    @Query("select Longitude from( SELECT DISTINCT EnglishName, Longitude FROM Stations)")
    List<Float> longs();
    @Query("select ArabicName from Stations where Line = 'line 1'")
    List<String> line_1_arabic();
    @Query("select ArabicName FROM Stations where Line = 'line 2'")
    List<String> line_2_arabic();
    @Query("select ArabicName FROM Stations where Line = 'line 3'")
    List<String> line_3_arabic();
    @Query("select DISTINCT ArabicName FROM Stations")
    List<String> lines_all_arabic();
}
