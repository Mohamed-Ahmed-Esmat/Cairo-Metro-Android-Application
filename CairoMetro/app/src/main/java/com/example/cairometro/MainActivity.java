package com.example.cairometro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;

import android.net.Uri;
import java.util.List;
import java.util.Locale;

import mumayank.com.airlocationlibrary.AirLocation;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, AirLocation.Callback, NavigationView.OnNavigationItemSelectedListener {
    TextView start;
    TextView end;
    TextView stationName;

    TextView distance;
    TextView price;
    TextView time;

    TextView views;
    ListView route;
    Button view_btn;

    ImageView vibrateOn;
    ImageView soundOn;
    ImageView mapImage;
    ImageView timeImage;
    ImageView costImage;
    boolean vibrate_on = true;
    boolean sound_on = true;
    boolean shakeStatus, comfortStatus;
    String langChosen = "English" , lastLang = "English", langCode;
    String end_station, start_station;

    String attaba_index, alshohadaa_index, sadat_index, nasser_index;

    float dist = 0;
    Dialog dialog;
    SharedPreferences pref;
    AirLocation airLocation;

    TextToSpeech tts;
    Location loc1 = new Location("");

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    ArrayList<String> line_1 = new ArrayList<>();
    ArrayList<String> line_2 = new ArrayList<>();

    ArrayList<String> line_3 = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> lines_all = new ArrayList<>();
//    ArrayList<Float> distances = new ArrayList<>();
    ArrayList<Float> lats = new ArrayList<>();
    ArrayList<Float> longs = new ArrayList<>();
    ArrayList<ArrayList<String>> metrolines = new ArrayList<ArrayList<String>>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        line_1 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_1();
        line_2 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_2();
        line_3 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_3();
        lines_all = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().lines_all();
        lats = (ArrayList<Float>) StationDatabase.getInstance(this).getStationDAO().lats();
        longs = (ArrayList<Float>) StationDatabase.getInstance(this).getStationDAO().longs();
        pref = getPreferences(MODE_PRIVATE);
        lastLang = pref.getString("currentLang", "English");
        langChosen = getIntent().getStringExtra("langChosen");
        System.out.println("Language status = "+langChosen);
        if(langChosen == null)
            langChosen = lastLang;
        if( langChosen!=null&&langChosen.equals("Arabic")){
            line_1 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_1_arabic();
            line_2 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_2_arabic();
            line_3 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_3_arabic();
            lines_all = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().lines_all_arabic();
            langCode = "ar";
        }
        else if( langChosen!=null&&langChosen.equals("English")){
            line_1 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_1();
            line_2 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_2();
            line_3 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_3();
            lines_all = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().lines_all();
            langCode = "en";
        }
        System.out.println("Line 1 length = "+line_1.size());
        System.out.println("Line 2 length = "+line_2.size());
        System.out.println("Line 3 length = "+line_3.size());
        System.out.println("Lines length = "+lines_all);
        System.out.println("Latitude length = "+lats.size());
        System.out.println("Shake mode is "+shakeStatus);
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        setContentView(R.layout.activity_main);
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Sensey.getInstance().init(this);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        distance = findViewById(R.id.distance);
        price = findViewById(R.id.price);
        time = findViewById(R.id.time);
        views = findViewById(R.id.view);
        view_btn = findViewById(R.id.View);
//        vibrateOn = findViewById(R.id.vibrateOn);
//        soundOn = findViewById(R.id.soundOn);
        mapImage = findViewById(R.id.mapImage);
        timeImage = findViewById(R.id.timeImage);
        costImage = findViewById(R.id.costImage);
        route = findViewById(R.id.route);
        mapImage.setVisibility(View.INVISIBLE);
        timeImage.setVisibility(View.INVISIBLE);
        costImage.setVisibility(View.INVISIBLE);
        tts = new TextToSpeech(this, this);



        shakeStatus = getIntent().getBooleanExtra("shakeState", false);
        comfortStatus = getIntent().getBooleanExtra("comfortState", false);

        System.out.println("Line 1"+line_1);
         nasser_index = line_1.get(19);
        attaba_index = line_2.get(11);
         alshohadaa_index = line_1.get(21);
         sadat_index = line_1.get(18);
        if(shakeStatus)
            Sensey.getInstance().startShakeDetection(20,500,shakeListener);
        else
            Sensey.getInstance().stopShakeDetection(shakeListener);

        if(sound_on && !(start.getText().toString().equals(""))){
            tts.speak(start.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
        }

        if(sound_on && !(end.getText().toString().equals(""))){
            tts.speak(end.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
        }

        // Declaration of stations into line 1
//        line_1.addAll(List.of(
//                "helwan", "ain helwan", "helwan university", "wadi hof", "hadayek helwan", "el-maasara",
//                "tora el-asmant", "kozzika", "tora el-balad", "sakanat el-maadi", "maadi", "hadayek el-maadi",
//                "dar el-salam", "el-zahraa", "mar girgis", "el-malek el-saleh", "al-sayeda zeinab", "saad zaghloul",
//                "sadat", "nasser", "orabi", "al-shohadaa", "ghamra", "el-demerdash", "manshiet el-sadr",
//                "kobri el-qobba", "hammamat el-qobba", "saray el-qobba", "hadayeq el-zaitoun", "helmeyet el-zaitoun",
//                "el-matareyya", "ain shams", "ezbet el-nakhl", "el-marg", "new el-marg"
//        ));

        metrolines.add(line_1);
//
//        line_2.addAll(List.of(
//                "el-mounib", "sakiat mekky", "omm el-masryeen", "el giza", "faisal", "cairo university",
//                "el bohoth", "dokki", "opera", "sadat", "mohamed naguib", "attaba", "al-shohadaa",
//                "masarra", "road el-farag", "st. teresa", "khalafawy", "mezallat", "kolleyyet el-zeraa",
//                "shobra el-kheima"
//        ));

        metrolines.add(line_2);

//        line_3.addAll(List.of(
//                "adly mansour", "el haykestep", "omar ibn el-khattab", "qobaa", "hesham barakat",
//                "el-nozha", "nadi el-shams", "alf maskan", "heliopolis square", "haroun",
//                "al-ahram", "koleyet el-banat", "stadium", "fair zone", "abbassia", "abdou pasha",
//                "el geish", "bab el shaaria", "attaba", "nasser", "maspero", "safaa hegazy", "kit kat"
//        ));



        metrolines.add(line_3);

//        lines_all.addAll(List.of(
//                "helwan", "ain helwan", "helwan university", "wadi hof", "hadayek helwan", "el-maasara",
//                "tora el-asmant", "kozzika", "tora el-balad", "sakanat el-maadi", "maadi", "hadayek el-maadi",
//                "dar el-salam", "el-zahraa", "mar girgis", "el-malek el-saleh", "al-sayeda zeinab", "saad zaghloul",
//                "sadat", "nasser", "orabi", "al-shohadaa", "ghamra", "el-demerdash", "manshiet el-sadr",
//                "kobri el-qobba", "hammamat el-qobba", "saray el-qobba", "hadayeq el-zaitoun", "helmeyet el-zaitoun",
//                "el-matareyya", "ain shams", "ezbet el-nakhl", "el-marg", "new el-marg","el-mounib", "sakiat mekky", "omm el-masryeen", "el giza", "faisal", "cairo university",
//                "el bohoth", "dokki", "opera", "mohamed naguib", "attaba",
//                "masarra", "road el-farag", "st. teresa", "khalafawy", "mezallat", "kolleyyet el-zeraa",
//                "shobra el-kheima","adly mansour", "el haykestep", "omar ibn el-khattab", "qobaa", "hesham barakat",
//                "el-nozha", "nadi el-shams", "alf maskan", "heliopolis square", "haroun",
//                "al-ahram", "koleyet el-banat", "stadium", "fair zone", "abbassia", "abdou pasha",
//                "el geish", "bab el shaaria",  "maspero", "safaa hegazy", "kit kat"
//        ));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                dialog=new Dialog(MainActivity.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout(800,800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,lines_all);

                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        start.setText(adapter.getItem(position));

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                dialog=new Dialog(MainActivity.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout(800,800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,lines_all);

                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        end.setText(adapter.getItem(position));

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });
        pref = getPreferences(MODE_PRIVATE);
        String start_prev =pref.getString("station_1", "");
        String end_prev = pref.getString("station_2", "");
        start.setText(start_prev);
        end.setText(end_prev);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // Start the PlanTrip activity
            Intent intent = new Intent(MainActivity.this, PlanTrip.class);
            intent.putStringArrayListExtra("lines_all", new ArrayList<>(lines_all));
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor e = pref.edit();
        e.putString("station_1", start.getText().toString());
        e.putString("station_2", end.getText().toString());
        e.putString("currentLang", langChosen);
        e.apply();
        tts.stop();
        tts.shutdown();
        Sensey.getInstance().stop();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor e = pref.edit();
        e.putString("station_1", start.getText().toString());
        e.putString("station_2", end.getText().toString());
        e.putString("currentLang", langChosen);
        e.apply();
        tts.stop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        tts.stop();
        SharedPreferences.Editor e = pref.edit();
        e.putString("station_1", start.getText().toString());
        e.putString("station_2", end.getText().toString());
        e.putString("currentLang", langChosen);
        e.apply();
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean check_stations(String start, String end, ArrayList<String> line_1, ArrayList<String> line_2, ArrayList<String> line_3) {
        return (line_1.contains(start) || line_2.contains(start) || line_3.contains(start)) &&
                (line_1.contains(end) || line_2.contains(end) || line_3.contains(end));
    }

    public int distance_between(String start, String end, ArrayList<String> line_1) {
        int start_index = line_1.indexOf(start);
        int end_index = line_1.indexOf(end);
        return end_index - start_index;
    }

    public int print_stations(String start, String end, ArrayList<String> line_1) {
        System.out.println("Start station = "+start);
        int start_index = line_1.indexOf(start);
        System.out.println("End station = "+end);
        int end_index = line_1.indexOf(end);
        System.out.println("Start index = "+start_index);
        System.out.println("End index = "+end_index);
        int count = 1;
        if (start_index < end_index) {
            for (int i = start_index; i <= end_index && i < line_1.size(); i++) {
                String stationName = line_1.get(i);
                data.add(stationName);
                count++;
            }
        } else if (start_index > end_index) {

            for (int i = start_index; i >= end_index && i < line_1.size(); i--) {
                String stationName = line_1.get(i);
                data.add(stationName);
                count++;
            }
        }
        return count;
    }

    public void pricing(int no_of_stations) {
        if (no_of_stations <= 9)
            price.setText(R.string.five_pounds);
        else if (no_of_stations > 9 && no_of_stations <= 16)
            price.setText(R.string.seven_pounds);
        else
            price.setText(R.string.ten_pounds);
    }


    public int which_path(String start, String end, ArrayList<String> line_1, ArrayList<String> line_2, ArrayList<String> line_3){
        if ((line_1.contains(start) && line_1.contains(end)) ||
                (line_2.contains(start) && line_2.contains(end)) ||
                (line_3.contains(start) && line_3.contains(end))) {
            return 1;
        } else {
            return 0;
        }
    }


    public String getStationLine(String station, String end, ArrayList<ArrayList<String>> lines) {
        byte count=0, line = 0;
        byte end_i=0;
        byte count_1 = 0;
        for (byte i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains(station)) {
                count++;
                if((i+1) == 1)
                    line++;
                else if((i+1) == 2)
                    line+=2;
                else if((i+1) == 3)
                    line+=3;
            }
            if (lines.get(i).contains(end)) {
                count_1++;
                if((i+1) == 1)
                    end_i++;
                else if((i+1) == 2)
                    end_i+=2;
                else if((i+1) == 3)
                    end_i+=3;
            }
        }
        switch (count) {
            case 0:
                return "Station '" + station + "' does not belong to any of the provided lines.";

            case 1:
                if (line == 1)
                    return "Line " + line;
                else if (line == 2)
                    return "Line " + line;
                else if (line == 3)
                    return "Line " + line;

            case 2:
                if(count_1==2)
                    return "Both are transimition";
                if (end_i == 1)
                    return "Line " + end_i;
                else if (end_i == 2)
                    return "Line " + end_i;
                else if (end_i == 3)
                    return "Line " + end_i;

            default:
                return "hoooooooo";
        }


    }


    public int transmition_viewer(int line_no_1, int line_no_2, String start, String end, String trans_station, String line_start,ArrayList<String> line_1, String station_11, String station_12, ArrayList<String> line_2, String station_21, String station_22,boolean view){
        if(line_start.equals("Line "+line_no_1)) {
            int distance_1 = distance_between(start, trans_station, line_1);
            int distance_2 = distance_between(trans_station, end, line_2);
            if(distance_1<0)
                distance_1*=-1;
            if(distance_2<0)
                distance_2*=-1;
            int total_distance = distance_1 + distance_2;
            if(view == true) {
                data.add(getString(R.string.path_in_line) + line_no_1 + ": ");
                direction_viewer(start, trans_station, station_11, station_12, line_1);
                print_stations(start, trans_station, line_1);
                data.add(getString(R.string.transmit_from) + trans_station + getString(R.string.here_is_path_in_line) + line_no_2 + ": ");
                direction_viewer(trans_station, end, station_21, station_22, line_2);
                print_stations(trans_station, end, line_2);
            }

            return total_distance;
        }
        else if(line_start.equals("Line "+line_no_2)){
            int distance_1 = distance_between(start, trans_station, line_2);
            int distance_2 = distance_between(trans_station, end, line_1);
            if(distance_1<0)
                distance_1*=-1;
            if(distance_2<0)
                distance_2*=-1;
            int total_distance = distance_1 + distance_2;
            if(view == true) {
                data.add(getString(R.string.path_in_line)+ line_no_2 + ": ");
                direction_viewer(start, trans_station, station_21, station_22, line_2);
                print_stations(start, trans_station, line_2);
                data.add(getString(R.string.transmit_from) + trans_station + getString(R.string.here_is_path_in_line) + line_no_1 + ": ");
                direction_viewer(trans_station, end, station_11, station_12, line_1);
                print_stations(trans_station, end, line_1);
            }
            return total_distance;
        }
        return 0;
    }


    public int shortest_route(int distance1, int distance2,int distance3){
        int min =  Math.min(distance1, Math.min(distance2, distance3));
        if(distance1 == distance2 && distance2 == distance3 && distance1 == distance3){
            return distance1;
        }
        else if(distance1 == distance2)
            return distance1;

        else if(min == distance1){
           return distance1;
        } else if (min == distance2) {
            return distance2;
        }
        else if(min == distance3){
            return distance3;
        }
        return 0;
    }

    public void direction_viewer(String start,String end, String direction_1, String direction_2, ArrayList<String> line){
        int distance = distance_between(start, end, line);
        if (distance < 0) {
            distance = (distance) * -1;
            data.add(getString(R.string.direction)+" "+direction_1);

        } else {
            data.add(getString(R.string.direction)+" "+direction_2);
        }
    }


    public int indirect_transmition_viewer(int line1, int inter_line, int line2, String start, String end, String line_start, String trans_1, String trans_2, String direction_11, String direction_12, ArrayList<String> starting_line, String direction_21, String direction_22, ArrayList<String> interm_line, String direction_31, String direction_32, ArrayList<String> destination_line, boolean view){
        if(line_start.equals("Line "+line1)){
            int distance1 = distance_between(start, trans_1, starting_line);
            int distance2 = distance_between(trans_1, trans_2, interm_line);
            int distance3 = distance_between(trans_2, end, destination_line);
            if(distance1<0)
                distance1*=-1;
            if(distance2<0)
                distance2*=-1;
            if(distance3<0)
                distance3*=-1;
            if(view == true) {

                data.add(getString(R.string.go_to) + trans_1 + getString(R.string.station_to_transmit_to_line) + inter_line);
                direction_viewer(start, trans_1, direction_11, direction_12, starting_line);
                print_stations(start, trans_1, starting_line);

                data.add(getString(R.string.then_go_from) + trans_1 + getString(R.string.to) + trans_2 + getString(R.string.station_to_transmit_to_line) + line2);
                direction_viewer(trans_1, trans_2, direction_21, direction_22, interm_line);
                print_stations(trans_1, trans_2, interm_line);

                data.add(getString(R.string.here_s_you_route_in_line) + line2);
                direction_viewer(trans_2, end, direction_31, direction_32, destination_line);
                print_stations(trans_2, end, destination_line);
            }


            return distance1 + distance2 + distance3;
        }
        else if(line_start.equals("Line "+line2)){//test
            int distance1 = distance_between(start, trans_2, destination_line);
            int distance2 = distance_between(trans_2, trans_1, interm_line);
            int distance3 = distance_between(trans_1, end, starting_line);
            if(distance1<0)
                distance1*=-1;
            if(distance2<0)
                distance2*=-1;
            if(distance3<0)
                distance3*=-1;
            if(view == true) {

                data.add(getString(R.string.go_to) + trans_2 + getString(R.string.station_to_transmit_to_line) + inter_line);
                direction_viewer(start, trans_2, direction_31, direction_32, destination_line);
                print_stations(start, trans_2, destination_line);

                data.add(getString(R.string.then_go_from) + trans_2 + getString(R.string.to) + trans_1 + getString(R.string.station_to_transmit_to_line) + line1);
                direction_viewer(trans_2, trans_1, direction_21, direction_22, interm_line);
                print_stations(trans_2, trans_1, interm_line);

                data.add(getString(R.string.here_s_you_route_in_line) + line1);
                direction_viewer(trans_1, end, direction_11, direction_12, starting_line);
                print_stations(trans_1, end, starting_line);
            }

            return distance1 + distance2 + distance3;
        }
        return 0;
    }

    public void trans_station_path(int line_exists_1, int line_exists_2, int line_destination, String start, String end, String target_station, String trans_station_1, String trans_station_2, String trans_station_3, ArrayList<String> line_1, String station_11, String station_12, ArrayList<String> dest_line, String station_21, String station_22, ArrayList<String> line_3, String station_31, String station_32 ){
        if (start.equals(target_station)){


            int dist_min = shortest_route(transmition_viewer(line_exists_1, line_destination, start, end, trans_station_1, "Line "+line_exists_1, line_1, station_11, station_12, dest_line, station_21, station_22, false)
                    , transmition_viewer(line_exists_2, line_destination, start, end, trans_station_2, "Line "+line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, false)
                    , transmition_viewer(line_exists_2, line_destination, start, end, trans_station_3, "Line " + line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, false));
            if(dist_min == transmition_viewer(line_exists_1, line_destination, start, end, trans_station_1, "Line "+line_exists_1, line_1, station_11, station_12, dest_line, station_21, station_22, false)){
//                data.add("Shortest route is: ");
                int distance_1 = transmition_viewer(line_exists_1, line_destination, start, end, trans_station_1, "Line "+line_exists_1, line_1, station_11, station_12, dest_line, station_21, station_22, true);
                if(distance_1 < 0)
                    distance_1*=-1;
                distance.setText( distance_1 + getString(R.string.stations));
                time.setText((distance_1 * 2) + getString(R.string.minutes));
                pricing(distance_1 + 1);
            }
            else if(dist_min == transmition_viewer(line_exists_2, line_destination, start, end, trans_station_2, "Line "+line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, false)){
//                data.add("Shortest route is: ");
                int distance_2 = transmition_viewer(line_exists_2, line_destination, start, end, trans_station_2, "Line "+line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, true);
                if(distance_2 < 0)
                    distance_2*=-1;
                distance.setText( distance_2 + getString(R.string.stations));
                time.setText( (distance_2 * 2) + getString(R.string.minutes));
                pricing(distance_2 + 1);
            }
            else if (dist_min == transmition_viewer(line_exists_2, line_destination, start, end, trans_station_3, "Line " + line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, false) && line_destination !=3) {
//                data.add("Shortest route is: ");
                int distance_3 = transmition_viewer(line_exists_2, line_destination, start, end, trans_station_3, "Line " + line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, true);
                if(distance_3 < 0)
                    distance_3*=-1;
                distance.setText( distance_3 + getString(R.string.stations));
                time.setText( (distance_3 * 2) + getString(R.string.minutes));
                pricing(distance_3 + 1);
            }


        }
        else if (end.equals(target_station)) {

            int dist_min = shortest_route(transmition_viewer(line_destination, line_exists_1, start, end, trans_station_1, "Line " + line_destination, dest_line, station_21, station_22, line_1, station_11, station_12, false)
                    , transmition_viewer(line_destination, line_exists_2, start, end, trans_station_2, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, false)
                    , transmition_viewer(line_destination, line_exists_2, start, end, trans_station_3, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, false));
            if(dist_min == transmition_viewer(line_destination, line_exists_1, start, end, trans_station_1, "Line " + line_destination, dest_line, station_21, station_22, line_1, station_11, station_12, false)){
//                data.add("Shortest route is: ");
                int distance_1 = transmition_viewer(line_destination, line_exists_1, start, end, trans_station_1, "Line " + line_destination, dest_line, station_21, station_22, line_1, station_11, station_12, true);
                if(distance_1 < 0)
                    distance_1*=-1;
                distance.setText( distance_1 + getString(R.string.stations));
                time.setText( (distance_1 * 2) + getString(R.string.minutes));
                pricing(distance_1 + 1);
            }
            else if(dist_min == transmition_viewer(line_destination, line_exists_2, start, end, trans_station_2, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, false)){
//                data.add("Shortest route is: ");
                int distance_2 = transmition_viewer(line_destination, line_exists_2, start, end, trans_station_2, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, true);
                if(distance_2 < 0)
                    distance_2*=-1;
                distance.setText( distance_2 + getString(R.string.stations));
                time.setText( (distance_2 * 2) + getString(R.string.minutes));
                pricing(distance_2 + 1);
            }
            else if (dist_min == transmition_viewer(line_destination, line_exists_2, start, end, trans_station_3, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, false) && line_destination !=3) {
//                data.add("Shortest route is: ");
                int distance_3 = transmition_viewer(line_destination, line_exists_2, start, end, trans_station_3, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, true);
                if(distance_3 < 0)
                    distance_3*=-1;
                distance.setText( distance_3 + getString(R.string.stations));
                time.setText( (distance_3 * 2) + getString(R.string.minutes));
                pricing(distance_3 + 1);
            }
        }
    }

    public void submit(View view) {
        data.clear();
        route.setVisibility(View.VISIBLE);
        StationAdapter adapter = new StationAdapter(this, data);
        route.setAdapter(adapter);
        start_station = start.getText().toString();
        end_station = end.getText().toString();

if(start_station.equals("") || end_station.equals("")) {
    if (start_station.equals("")) {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(0)
                .playOn(findViewById(R.id.start));
    }

    if (end_station.equals("")) {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(0)
                .playOn(findViewById(R.id.end));

    }
    return;
}



        if(start_station.equalsIgnoreCase(end_station)) {
            Toast.makeText(this, getString(R.string.well_you_chose_the_same_stations), Toast.LENGTH_SHORT).show();
            return;
        }


        views.setText(R.string.to_view_the_alternative_routes_click_view);
        view_btn.setVisibility(View.VISIBLE);
        mapImage.setVisibility(View.VISIBLE);
        timeImage.setVisibility(View.VISIBLE);
        costImage.setVisibility(View.VISIBLE);
        view_btn.setEnabled(true);
        if (check_stations(start_station, end_station, line_1, line_2, line_3)) {
            //Printing the results

            //Checking if the distance is positive or negative,accordingly, to decide the direction
            int distance1 = distance_between(start_station, end_station, line_1);
            int distance2 = distance_between(start_station, end_station, line_2);
            int distance3 = distance_between(start_station, end_station, line_3);
            String line_start = getStationLine(start_station,end_station, metrolines);
            String line_end = getStationLine(end_station, start_station, metrolines);

            if((which_path(start_station,end_station,line_1,line_2,line_3)==0) ){
                trans_station_path(3,2,1, start_station, end_station, attaba_index, nasser_index, sadat_index, alshohadaa_index, line_3, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_1, getString(R.string.helwan), getString(R.string.el_marg), line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima));
                trans_station_path(3,1,2, start_station, end_station, nasser_index, attaba_index, sadat_index, alshohadaa_index, line_3, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), line_1, getString(R.string.helwan), getString(R.string.el_marg));
                trans_station_path(1,2,3, start_station, end_station, sadat_index, nasser_index, attaba_index, alshohadaa_index,  line_1,  getString(R.string.helwan), getString(R.string.el_marg),line_3,  getString(R.string.adly_mansour), getString(R.string.kit_kat), line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima));
                trans_station_path(1,2,3, start_station, end_station, alshohadaa_index, nasser_index, attaba_index, alshohadaa_index,  line_1, getString(R.string.helwan), getString(R.string.el_marg),line_3,  getString(R.string.adly_mansour), getString(R.string.kit_kat), line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima));
                if((line_end.equals("Line 1") && line_start.equals("Line 2")) || (line_end.equals("Line 2") && line_start.equals("Line 1"))) {


                    int min_dist = shortest_route(transmition_viewer(1, 2, start_station, end_station, sadat_index, line_start, line_1, getString(R.string.helwan), getString(R.string.el_marg), line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), false),
                            transmition_viewer(1, 2, start_station, end_station, alshohadaa_index, line_start, line_1, getString(R.string.helwan), getString(R.string.el_marg), line_2,  getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), false),
                            indirect_transmition_viewer(1,3,2, start_station, end_station, line_start,  nasser_index, attaba_index, getString(R.string.helwan), getString(R.string.el_marg), line_1, "Adly Mansour", "Kit Kat", line_3,  getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), line_2, false)
                    );

                    if(min_dist == transmition_viewer(1, 2, start_station, end_station, sadat_index, line_start, line_1, getString(R.string.helwan), getString(R.string.el_marg), line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), false)){
//                        data.add("Shortest route is: ");
                        int distance_1 = transmition_viewer(1, 2, start_station, end_station, sadat_index, line_start, line_1, getString(R.string.helwan), getString(R.string.el_marg), line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), true);
                        if(distance_1 < 0)
                            distance_1*=-1;
                        distance.setText( distance_1 + getString(R.string.stations));
                        time.setText((distance_1 * 2) + getString(R.string.minutes));
                        pricing(distance_1 + 1);
                    }
                    else if(min_dist ==transmition_viewer(1, 2, start_station, end_station, alshohadaa_index, line_start, line_1, getString(R.string.helwan), getString(R.string.el_marg), line_2,  getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), false)){
//                        data.add("Shortest route is: ");
                        int distance_2 = transmition_viewer(1, 2, start_station, end_station,alshohadaa_index, line_start, line_1, getString(R.string.helwan), getString(R.string.el_marg), line_2,  getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), true);
                        if(distance_2 < 0)
                            distance_2*=-1;
                        distance.setText( distance_2 + getString(R.string.stations));
                        time.setText( (distance_2 * 2) + getString(R.string.minutes));
                        pricing(distance_2 + 1);
                    }
                    else if(min_dist ==indirect_transmition_viewer(1,3,2, start_station, end_station, line_start,  nasser_index, attaba_index, getString(R.string.helwan), getString(R.string.el_marg), line_1,  getString(R.string.adly_mansour),  getString(R.string.kit_kat), line_3,  getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), line_2, false)){
//                        data.add("Shortest route is: ");
                        int distance_3 = indirect_transmition_viewer(1,3,2, start_station, end_station, line_start,  nasser_index,  attaba_index, getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.adly_mansour),  getString(R.string.kit_kat), line_3,  getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), line_2, true);
                        if(distance_3 < 0)
                            distance_3*=-1;
                        distance.setText(distance_3 + getString(R.string.stations));
                        time.setText( (distance_3 * 2) + getString(R.string.minutes));
                        pricing(distance_3 + 1);

                    }
                }

                else if((line_end.equals("Line 1") && line_start.equals("Line 3")) || (line_end.equals("Line 3") && line_start.equals("Line 1"))) {

                    int min_dist = shortest_route(transmition_viewer(1, 3, start_station, end_station,nasser_index, line_start, line_1, getString(R.string.helwan), getString(R.string.el_marg), line_3, getString(R.string.adly_mansour), getString(R.string.kit_kat), false),
                            indirect_transmition_viewer(1,2,3, start_station, end_station, line_start, sadat_index, attaba_index, getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, false),
                            indirect_transmition_viewer(1,2,3, start_station, end_station, line_start, alshohadaa_index, attaba_index, getString(R.string.helwan), getString(R.string.el_marg), line_1,getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, false)
                    );

                    if(min_dist == transmition_viewer(1, 3, start_station, end_station, nasser_index, line_start, line_1, getString(R.string.helwan), getString(R.string.el_marg), line_3, getString(R.string.adly_mansour), getString(R.string.kit_kat), false)){
//                        data.add("Shortest route is: ");
                        int distance_1 = transmition_viewer(1, 3, start_station, end_station, nasser_index, line_start, line_1, getString(R.string.helwan), getString(R.string.el_marg), line_3, getString(R.string.adly_mansour), getString(R.string.kit_kat), true);
                        if(distance_1 < 0)
                            distance_1*=-1;
                        distance.setText( distance_1 + getString(R.string.stations));
                        time.setText((distance_1 * 2) + getString(R.string.minutes));
                        pricing(distance_1 + 1);
                    }
                    else if(min_dist == indirect_transmition_viewer(1,2,3, start_station, end_station, line_start, sadat_index, attaba_index, getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, false)){
//                        data.add("Shortest route is: ");
                        int distance_2 = indirect_transmition_viewer(1,2,3, start_station, end_station, line_start, sadat_index, attaba_index, getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, false);
                        if(distance_2 < 0)
                            distance_2*=-1;
                        distance.setText(distance_2 + getString(R.string.stations));
                        time.setText((distance_2 * 2) + getString(R.string.minutes));
                        pricing(distance_2 + 1);
                    }
                    else if(min_dist == indirect_transmition_viewer(1,2,3, start_station, end_station, line_start, alshohadaa_index, attaba_index, getString(R.string.helwan), getString(R.string.el_marg), line_1,getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, false)){
//                        data.add("Shortest route is: ");
                        int distance_3 = indirect_transmition_viewer(1,2,3, start_station, end_station, line_start,alshohadaa_index, attaba_index, getString(R.string.helwan), getString(R.string.el_marg), line_1,getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, true);
                        if(distance_3 < 0)
                            distance_3*=-1;
                        distance.setText(distance_3 + getString(R.string.stations));
                        time.setText((distance_3 * 2) + getString(R.string.minutes));
                        pricing(distance_3 + 1);

                    }
                }

                else if((line_end.equals("Line 2") && line_start.equals("Line 3")) || (line_end.equals("Line 3") && line_start.equals("Line 2"))) {
                    int minm_dist = shortest_route( transmition_viewer(2, 3, start_station, end_station, attaba_index, line_start, line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), line_3, getString(R.string.adly_mansour), getString(R.string.kit_kat), false),
                            indirect_transmition_viewer(2,1,3, start_station, end_station, line_start,sadat_index, nasser_index, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2,getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, false),
                            indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, alshohadaa_index, nasser_index, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2,getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, false)
                    );

                    if(minm_dist == transmition_viewer(2, 3, start_station, end_station, attaba_index, line_start, line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), line_3, getString(R.string.adly_mansour), getString(R.string.kit_kat), false)){
//                        data.add("Shortest route is: ");
                        int distance_1 = transmition_viewer(2, 3, start_station, end_station, attaba_index, line_start, line_2, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima), line_3, getString(R.string.adly_mansour), getString(R.string.kit_kat), true);
                        if(distance_1 < 0)
                            distance_1*=-1;
                        distance.setText( distance_1 + getString(R.string.stations));
                        time.setText((distance_1 * 2) + getString(R.string.minutes));
                            pricing(distance_1 + 1);
                        }
                    else if(minm_dist ==  indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, sadat_index, nasser_index, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2,getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, false)){
//                        data.add("Shortest route is: ");
                        int distance_2 =  indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, sadat_index, nasser_index, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2,getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, true);
                        if(distance_2 < 0)
                            distance_2*=-1;
                        distance.setText(distance_2 + getString(R.string.stations));
                        time.setText((distance_2 * 2) + getString(R.string.minutes));
                            pricing(distance_2 + 1);
                        }
                    else if(minm_dist == indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, alshohadaa_index, nasser_index, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2,getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, false))
                    {
//                        data.add("Shortest route is: ");
                        int distance_3 = indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, alshohadaa_index, nasser_index, getString(R.string.el_mounib), getString(R.string.shobra_el_kheima) , line_2,getString(R.string.helwan), getString(R.string.el_marg), line_1, getString(R.string.adly_mansour), getString(R.string.kit_kat), line_3, true);
                        if(distance_3 < 0)
                            distance_3*=-1;
                        distance.setText(distance_3 + getString(R.string.stations));
                        time.setText((distance_3 * 2) + getString(R.string.minutes));
                                pricing(distance_3 + 1);

                    }
                }
            }
            else if(line_start.equals("Line 1") || start_station.equals(getString(R.string.sadat)) && end_station.equals(getString(R.string.al_shohadaa)) || start_station.equals(getString(R.string.al_shohadaa)) && end_station.equals(getString(R.string.sadat)) ||start_station.equals(getString(R.string.nasser)) && end_station.equals(getString(R.string.al_shohadaa)) || (start_station.equals(getString(R.string.al_shohadaa)) && end_station.equals("nasser")) || start_station.equals(getString(R.string.nasser)) && end_station.equals(getString(R.string.sadat)) || (start_station.equals(getString(R.string.sadat)) && end_station.equals(getString(R.string.nasser)))){
                direction_viewer(start_station, end_station, getString(R.string.helwan), getString(R.string.el_marg), line_1);
                if(distance1<0)
                    distance1*=-1;
                distance.setText( distance1 + getString(R.string.stations));
                int count = print_stations(start_station, end_station, line_1);
                time.setText( (distance1 * 2) + getString(R.string.minutes));
                pricing(count-1);
            }
            else if(line_start.equals("Line 2") || start_station.equals(getString(R.string.attaba)) && end_station.equals(getString(R.string.al_shohadaa)) || (start_station.equals(getString(R.string.al_shohadaa)) && end_station.equals(getString(R.string.attaba))) || start_station.equals(getString(R.string.attaba)) && end_station.equals(getString(R.string.sadat)) || start_station.equals(getString(R.string.sadat)) && end_station.equals(getString(R.string.attaba))){
                direction_viewer(start_station, end_station, getString(R.string.el_mounib) , getString(R.string.shobra_el_kheima), line_2);
                if(distance2<0)
                    distance2*=-1;
                distance.setText(distance2 +getString(R.string.stations));
                int count = print_stations(start_station, end_station, line_2);
                time.setText((distance2 * 2) +getString(R.string.stations));
                pricing(count-1);
            }
            else if(line_start.equals("Line 3") || (start_station.equals(getString(R.string.attaba)) && end_station.equals(getString(R.string.nasser))) || (start_station.equals(getString(R.string.nasser)) && end_station.equals(getString(R.string.attaba)))){
                direction_viewer(start_station, end_station, getString(R.string.adly_mansour) ,getString(R.string.kit_kat), line_3);
                if(distance3<0)
                    distance3*=-1;
                distance.setText(distance3 + getString(R.string.stations));
                int count = print_stations(start_station, end_station, line_3);
                time.setText((distance3 * 2) +getString(R.string.minutes));
                pricing(count-1);
            }
        }
    }

    ShakeDetector.ShakeListener shakeListener= new ShakeDetector.ShakeListener() {
        @Override
        public void onShakeDetected() {

        }


        @Override
        public void onShakeStopped() {
            start.setText("");
            end.setText("");
            distance.setText("");
            time.setText("");
            price.setText("");
            view_btn.setEnabled(false);
            view_btn.setVisibility(View.INVISIBLE);
            mapImage.setVisibility(View.INVISIBLE);
            timeImage.setVisibility(View.INVISIBLE);
            costImage.setVisibility(View.INVISIBLE);
            route.setVisibility(View.INVISIBLE);
            views.setText("");
        }

    };


    public void view(View view) {
        Intent intent = new Intent(MainActivity.this, View_Route.class);
        intent.putExtra("start_station", start.getText().toString());
        intent.putExtra("end_station", end.getText().toString());
        startActivity(intent);

    }

    public void swap(View view) {
        String start_station = start.getText().toString();
        start.setText(end.getText().toString());
        end.setText(start_station);
    }

//    public void change(View view) {
//        if(vibrate_on){
//            vibrateOn.setImageResource(R.drawable.vibrate_off);
//            vibrate_on = false;
//            Sensey.getInstance().stopShakeDetection(shakeListener);
//        }
//        else{
//            vibrateOn.setImageResource(R.drawable.vibrate_on);
//            vibrate_on = true;
//            Sensey.getInstance().startShakeDetection(20,500,shakeListener);
//            YoYo.with(Techniques.Shake).duration(700).playOn(vibrateOn);
//        }
//    }
//
//    public void change2(View view) {
//        if(sound_on){
//            soundOn.setImageResource(R.drawable.sound_off);
//            sound_on = false;
//        }
//        else{
//            soundOn.setImageResource(R.drawable.sound_on);
//            sound_on = true;
//        }
//
//    }

    class StationAdapter extends ArrayAdapter<String>{
        public StationAdapter(@NonNull Context context, List<String> data){
            super(context, 0, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            convertView=getLayoutInflater().inflate(R.layout.custom_view, parent, false);
            stationName = convertView.findViewById(R.id.stationName);
            stationName.setText(getItem(position));
            return convertView;
        }
    }

    @Override
    public void onInit(int i) {
        tts.setLanguage(new Locale("ar"));
        tts.setPitch(1F);

    }



    public void play(View view) {
        String start_station = start.getText().toString();
        if(sound_on && !(start_station.equals(""))) {
            tts.speak("You will go from "+start_station, TextToSpeech.QUEUE_ADD, null, null);
        }
        String end_station = end.getText().toString();
        if(sound_on && !(end_station.equals(""))) {
            tts.speak(" to "+end_station, TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    public void mapViewStart(View view) {
        Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+start.getText().toString()+" station cairo"));
        startActivity(a);
    }

    public void mapViewEnd(View view) {
        Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+end.getText().toString()+" station cairo"));
        startActivity(a);
    }

    public void view_nearest_station(ArrayList<String> line){
        float min = 9999;
        byte i = 0;
        int minIndex = 0;
        for(String station : line){
            Location loc2 = new Location("");
            loc2.setLatitude(lats.get(i));
            loc2.setLongitude(longs.get(i));
            dist = loc1.distanceTo(loc2);
            dist/=1000;
            if(dist<min){
                min = dist;
                minIndex = line.indexOf(station);
            }
            i++;
        }
        start.setText(line.get(minIndex));
    }
    public void where(View view) {
        airLocation = new AirLocation(this, this, true, 0, "");
        airLocation.start();
    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
        Toast.makeText(this, "Location disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(@NonNull ArrayList<Location> arrayList) {
        end_station = end.getText().toString();
        double lat_home = arrayList.get(0).getLatitude();
        double lon_home = arrayList.get(0).getLongitude();
        loc1.setLatitude(lat_home);
        loc1.setLongitude(lon_home);

        if(comfortStatus) {
            if (line_1.contains(end_station)) {
                view_nearest_station(line_1);
            }
            else if (line_2.contains(end_station)) {
                view_nearest_station(line_2);
            }
            else if (line_3.contains(end_station)) {
                view_nearest_station(line_3);
            }
        }
        else
           view_nearest_station(lines_all);
        }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void settings(MenuItem item) {
        SharedPreferences.Editor e = pref.edit();
        e.putString("station_1", start.getText().toString());
        e.putString("station_2", end.getText().toString());
        e.apply();
        Intent a = new Intent(this, SettingsActivity.class);
        startActivity(a);
    }
}