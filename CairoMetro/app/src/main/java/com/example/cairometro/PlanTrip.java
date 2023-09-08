package com.example.cairometro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mumayank.com.airlocationlibrary.AirLocation;

public class PlanTrip extends AppCompatActivity implements AirLocation.Callback {
    EditText targetPlace;
    TextView desiredStation;
    AirLocation airLocation;
    ArrayList<String>lines_all;
    ArrayList<Float> lats = new ArrayList<>();
    ArrayList<Float> longs = new ArrayList<>();
    Location loc1 = new Location("");
    float dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_trip);
        targetPlace = findViewById(R.id.targetPlace);
        desiredStation = findViewById(R.id.desiredStation);
        lines_all = (ArrayList<String>) getIntent().getSerializableExtra("lines_all");
        lats = (ArrayList<Float>) StationDatabase.getInstance(this).getStationDAO().lats();
        longs = (ArrayList<Float>) StationDatabase.getInstance(this).getStationDAO().longs();
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void generate(View view) {
        airLocation = new AirLocation(this, this, true, 0, "");
        airLocation.start();
    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
        Toast.makeText(this, "Location disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(@NonNull ArrayList<Location> arrayList) {
        Geocoder geocoder = new Geocoder(this);
        float min = 9999;
        int minIndex = 0;
        byte i =0;

        try {
            List<Address> address1List = geocoder.getFromLocationName(targetPlace.getText().toString()+" egypt", 1);
            loc1.setLatitude(address1List.get(0).getLatitude());
            loc1.setLongitude(address1List.get(0).getLongitude());
            for(String station : lines_all){
                Location loc2 = new Location("");
                loc2.setLatitude(lats.get(i));
                loc2.setLongitude(longs.get(i));
                dist = loc1.distanceTo(loc2);
                dist/=1000;
                if(dist<min){
                    min = dist;
                    minIndex = lines_all.indexOf(station);
                }
                i++;
            }
            desiredStation.setText(getString(R.string.here_s_the_nearest_station_you_could_go_to)+lines_all.get(minIndex));
        } catch (IOException e) {
            Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
        }
    }
}