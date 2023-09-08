package com.example.cairometro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;

public class View_Route extends AppCompatActivity {
    ListView route;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> line_1 = new ArrayList<>();
    ArrayList<String> line_2 = new ArrayList<>();

    ArrayList<String> line_3 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_route);

        line_1 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_1();
        line_2 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_2();
        line_3 = (ArrayList<String>) StationDatabase.getInstance(this).getStationDAO().line_3();
        ArrayList<ArrayList<String>> metrolines = new ArrayList<>();
        data.clear();

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
        route = findViewById(R.id.route);
        Intent intent = getIntent();
        String start_station = intent.getStringExtra("start_station");
        String end_station = intent.getStringExtra("end_station");
        int distance1 = distance_between(start_station, end_station, line_1);
        int distance2 = distance_between(start_station, end_station, line_2);
        int distance3 = distance_between(start_station, end_station, line_3);
        String line_start = getStationLine(start_station,end_station, metrolines);
        String line_end = getStationLine(end_station, start_station, metrolines);


        if((which_path(start_station,end_station,line_1,line_2,line_3)==0) ){
            trans_station_path(3,2,1, start_station, end_station, "attaba", "nasser", "sadat", "al-shohadaa", line_3, "Adly Mansour", "Kit Kat", line_1, "Helwan", "El-Marg", line_2, "El-Mounib", "Shobra El-Kheima");
            trans_station_path(3,1,2, start_station, end_station, "nasser", "attaba", "sadat", "al-shohadaa", line_3, "Adly Mansour", "Kit Kat", line_2, "El-Mounib", "Shobra El-Kheima", line_1, "Helwan", "El-Marg");
            trans_station_path(1,2,3, start_station, end_station, "sadat", "nasser", "attaba", "al-shohadaa",  line_1, "Helwan", "El-Marg",line_3, "Adly Mansour", "Kit Kat", line_2, "El-Mounib", "Shobra El-Kheima");
            trans_station_path(1,2,3, start_station, end_station, "al-shohadaa", "nasser", "attaba", "al-shohadaa",  line_1, "Helwan", "El-Marg",line_3, "Adly Mansour", "Kit Kat", line_2, "El-Mounib", "Shobra El-Kheima");
            if((line_end.equals("Line 1") && line_start.equals("Line 2")) || (line_end.equals("Line 2") && line_start.equals("Line 1"))) {


                int min_dist = shortest_route(transmition_viewer(1, 2, start_station, end_station, "sadat", line_start, line_1, "Helwan", "El-Marg", line_2, "El-Mounib", "Shobra El-Kheima", false),
                        transmition_viewer(1, 2, start_station, end_station, "al-shohadaa", line_start, line_1, "Helwan", "El-Marg", line_2, "El-Mounib", "Shobra El-Kheima", false),
                        indirect_transmition_viewer(1,3,2, start_station, end_station, line_start, "nasser", "attaba", "Helwan", "El-Marg", line_1, "Adly Mansour", "Kit Kat", line_3, "El-Mounib", "Shobra El-Kheima", line_2, false)
                );

                if(min_dist == transmition_viewer(1, 2, start_station, end_station, "sadat", line_start, line_1, "Helwan", "El-Marg", line_2, "El-Mounib", "Shobra El-Kheima", false)){
                    data.add("Shortest route is: ");
                    int distance_1 = transmition_viewer(1, 2, start_station, end_station, "sadat", line_start, line_1, "Helwan", "El-Marg", line_2, "El-Mounib", "Shobra El-Kheima", true);
                    if(distance_1 < 0)
                        distance_1*=-1;
                }
                else if(min_dist == transmition_viewer(1, 2, start_station, end_station, "al-shohadaa", line_start, line_1, "Helwan", "El-Marg", line_2, "El-Mounib", "Shobra El-Kheima", false)){
                    data.add("Shortest route is: ");
                    int distance_2 = transmition_viewer(1, 2, start_station, end_station, "al-shohadaa", line_start, line_1, "Helwan", "El-Marg", line_2, "El-Mounib", "Shobra El-Kheima", true);
                    if(distance_2 < 0)
                        distance_2*=-1;

                }
                else if(min_dist ==indirect_transmition_viewer(1,3,2, start_station, end_station, line_start, "nasser", "attaba", "Helwan", "El-Marg", line_1, "Adly Mansour", "Kit Kat", line_3, "El-Mounib", "Shobra El-Kheima", line_2, false)){
                    data.add("Shortest route is: ");
                    int distance_3 = indirect_transmition_viewer(1,3,2, start_station, end_station, line_start, "nasser", "attaba", "Helwan", "El-Marg", line_1, "Adly Mansour", "Kit Kat", line_3, "El-Mounib", "Shobra El-Kheima", line_2, true);
                    if(distance_3 < 0)
                        distance_3*=-1;


                }
            }

            else if((line_end.equals("Line 1") && line_start.equals("Line 3")) || (line_end.equals("Line 3") && line_start.equals("Line 1"))) {

                int min_dist = shortest_route(transmition_viewer(1, 3, start_station, end_station, "nasser", line_start, line_1, "Helwan", "El-Marg", line_3, "Adly Mansour", "Kit Kat", false),
                        indirect_transmition_viewer(1,2,3, start_station, end_station, line_start, "sadat", "attaba", "Helwan", "El-Marg", line_1, "El-Mounib", "Shobra El-Kheima" , line_2, "Adly Mansour", "Kit Kat", line_3, false),
                        indirect_transmition_viewer(1,2,3, start_station, end_station, line_start, "al-shohadaa", "attaba", "Helwan", "El-Marg", line_1,"El-Mounib", "Shobra El-Kheima" , line_2, "Adly Mansour", "Kit Kat", line_3, false)
                );

                if(min_dist == transmition_viewer(1, 3, start_station, end_station, "nasser", line_start, line_1, "Helwan", "El-Marg", line_3, "Adly Mansour", "Kit Kat", false)){
                    data.add("Shortest route is: ");
                    int distance_1 = transmition_viewer(1, 3, start_station, end_station, "nasser", line_start, line_1, "Helwan", "El-Marg", line_3, "Adly Mansour", "Kit Kat", true);
                    if(distance_1 < 0)
                        distance_1*=-1;

                }
                else if(min_dist == transmition_viewer(1, 2, start_station, end_station, "al-shohadaa", line_start, line_1, "Helwan", "El-Marg", line_2, "El-Mounib", "Shobra El-Kheima", false)){
                    data.add("Shortest route is: ");
                    int distance_2 = transmition_viewer(1, 2, start_station, end_station, "al-shohadaa", line_start, line_1, "Helwan", "El-Marg", line_2, "El-Mounib", "Shobra El-Kheima", true);
                    if(distance_2 < 0)
                        distance_2*=-1;

                }
                else if(min_dist == indirect_transmition_viewer(1,2,3, start_station, end_station, line_start, "al-shohadaa", "attaba", "Helwan", "El-Marg", line_1,"El-Mounib", "Shobra El-Kheima" , line_2, "Adly Mansour", "Kit Kat", line_3, false)){
                    data.add("Shortest route is: ");
                    int distance_3 = indirect_transmition_viewer(1,2,3, start_station, end_station, line_start, "al-shohadaa", "attaba", "Helwan", "El-Marg", line_1,"El-Mounib", "Shobra El-Kheima" , line_2, "Adly Mansour", "Kit Kat", line_3, true);
                    if(distance_3 < 0)
                        distance_3*=-1;


                }
            }

            else if((line_end.equals("Line 2") && line_start.equals("Line 3")) || (line_end.equals("Line 3") && line_start.equals("Line 2"))) {
                int minm_dist = shortest_route( transmition_viewer(2, 3, start_station, end_station, "attaba", line_start, line_2, "El-Mounib", "Shobra El-Kheima", line_3, "Adly Mansour", "Kit Kat", false),
                        indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, "sadat", "nasser", "El-Mounib", "Shobra El-Kheima" , line_2,"Helwan", "El-Marg", line_1, "Adly Mansour", "Kit Kat", line_3, false),
                        indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, "al-shohadaa", "nasser", "El-Mounib", "Shobra El-Kheima" , line_2,"Helwan", "El-Marg", line_1, "Adly Mansour", "Kit Kat", line_3, false)
                );

                if(minm_dist == transmition_viewer(2, 3, start_station, end_station, "attaba", line_start, line_2, "El-Mounib", "Shobra El-Kheima", line_3, "Adly Mansour", "Kit Kat", false)){
                    data.add("Shortest route is: ");
                    int distance_1 = transmition_viewer(2, 3, start_station, end_station, "attaba", line_start, line_2, "El-Mounib", "Shobra El-Kheima", line_3, "Adly Mansour", "Kit Kat", true);
                    if(distance_1 < 0)
                        distance_1*=-1;

                }
                else if(minm_dist ==  indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, "sadat", "nasser", "El-Mounib", "Shobra El-Kheima" , line_2,"Helwan", "El-Marg", line_1, "Adly Mansour", "Kit Kat", line_3, false)){
                    data.add("Shortest route is: ");
                    int distance_2 =  indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, "sadat", "nasser", "El-Mounib", "Shobra El-Kheima" , line_2,"Helwan", "El-Marg", line_1, "Adly Mansour", "Kit Kat", line_3, true);
                    if(distance_2 < 0)
                        distance_2*=-1;

                }
                else if(minm_dist == indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, "al-shohadaa", "nasser", "El-Mounib", "Shobra El-Kheima" , line_2,"Helwan", "El-Marg", line_1, "Adly Mansour", "Kit Kat", line_3, false))
                {
                    data.add("Shortest route is: ");
                    int distance_3 = indirect_transmition_viewer(2,1,3, start_station, end_station, line_start, "al-shohadaa", "nasser", "El-Mounib", "Shobra El-Kheima" , line_2,"Helwan", "El-Marg", line_1, "Adly Mansour", "Kit Kat", line_3, true);
                    if(distance_3 < 0)
                        distance_3*=-1;


                }
            }
        }
        else if(line_start.equals("Line 1") || start_station.equals("sadat") && end_station.equals("al-shohadaa") || start_station.equals("al-shohadaa") && end_station.equals("sadat") ||start_station.equals("nasser") && end_station.equals("al-shohadaa") || (start_station.equals("al-shohadaa") && end_station.equals("nasser")) || start_station.equals("nasser") && end_station.equals("sadat") || (start_station.equals("sadat") && end_station.equals("nasser"))){
            direction_viewer(start_station, end_station, "Helwan", "El-Marg", line_1);
            if(distance1<0)
                distance1*=-1;

            int count = print_stations(start_station, end_station, line_1);

        }
        else if(line_start.equals("Line 2") || start_station.equals("attaba") && end_station.equals("al-shohadaa") || (start_station.equals("al-shohadaa") && end_station.equals("attaba")) || start_station.equals("attaba") && end_station.equals("sadat") || start_station.equals("sadat") && end_station.equals("attaba")){
            direction_viewer(start_station, end_station, "El-Mounib" , "Shobra El-Kheima", line_2);
            if(distance2<0)
                distance2*=-1;

            int count = print_stations(start_station, end_station, line_2);

        }
        else if(line_start.equals("Line 3") || (start_station.equals("attaba") && end_station.equals("nasser")) || (start_station.equals("nasser") && end_station.equals("attaba"))){
            direction_viewer(start_station, end_station, "Adly Mansour" ,"Kit Kat", line_3);
            if(distance3<0)
                distance3*=-1;

            int count = print_stations(start_station, end_station, line_3);

        }


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        route.setAdapter(adapter);
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
        int start_index = line_1.indexOf(start);
        int end_index = line_1.indexOf(end);
        int count = 1;
        if (start_index < end_index) {
            for (int i = start_index; i <= end_index && i < line_1.size(); i++) {
                String stationName = line_1.get(i);
                data.add(count + ". " + stationName);
                count++;
            }
        } else if (start_index > end_index) {
            for (int i = start_index; i >= end_index && i < line_1.size(); i--) {
                String stationName = line_1.get(i);
                data.add(count + ". " + stationName);
                count++;
            }
        }
        return count;
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


    public String getStationLine(String station,String end, ArrayList<ArrayList<String>> lines) {
        int count=0, line = 0;
        int end_i=0;
        int count_1 = 0;
        for (int i = 0; i < lines.size(); i++) {
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


    public int transmition_viewer(int line_no_1, int line_no_2, String start, String end, String trans_station,String line_start, ArrayList<String> line_1,String station_11,String station_12, ArrayList<String> line_2,String station_21,String station_22, boolean view){
        if(line_start.equals("Line "+line_no_1)) {
            int distance_1 = distance_between(start, trans_station, line_1);
            int distance_2 = distance_between(trans_station, end, line_2);
            if(distance_1<0)
                distance_1*=-1;
            if(distance_2<0)
                distance_2*=-1;
            int total_distance = distance_1 + distance_2;
            if(view == true) {
                data.add("Your path in Line " + line_no_1 + ": ");
                direction_viewer(start, trans_station, station_11, station_12, line_1);
                print_stations(start, trans_station, line_1);
                data.add("Now after transmiting from " + trans_station + " here is path in Line " + line_no_2 + ": ");
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
                data.add("Your path in Line " + line_no_2 + ": ");
                direction_viewer(start, trans_station, station_21, station_22, line_2);
                print_stations(start, trans_station, line_2);
                data.add("Now after transmiting from " + trans_station + " here is path in Line " + line_no_1 + ": ");
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
            data.add("You are in the direction of "+direction_1);

        } else {
            data.add("You are in the direction of "+direction_2);
        }
    }


    public int indirect_transmition_viewer(int line1, int inter_line, int line2, String start, String end,String line_start, String trans_1, String trans_2,String direction_11, String direction_12, ArrayList<String> starting_line,String direction_21, String direction_22, ArrayList<String> interm_line,String direction_31, String direction_32, ArrayList<String> destination_line, boolean view){
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
                direction_viewer(start, trans_1, direction_11, direction_12, starting_line);
                data.add("You will go to " + trans_1 + " station, to transmit to Line " + inter_line);
                print_stations(start, trans_1, starting_line);
                direction_viewer(trans_1, trans_2, direction_21, direction_22, interm_line);
                data.add("Then you will go from " + trans_1 + " to " + trans_2 + " to transit to Line " + line2);
                print_stations(trans_1, trans_2, interm_line);
                direction_viewer(trans_2, end, direction_31, direction_32, destination_line);
                data.add("Here's you route in Line " + line2);
                print_stations(trans_2, end, destination_line);
            }
            return distance1 + distance2 + distance3;
        }
        else if(line_start.equals("Line "+line2)){
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
                direction_viewer(start, trans_2, direction_31, direction_32, destination_line);
                data.add("You will go to " + trans_2 + " station, to transmit to Line " + inter_line);
                print_stations(start, trans_2, destination_line);
                direction_viewer(trans_2, trans_1, direction_21, direction_22, interm_line);
                data.add("Then you will go from " + trans_2 + " to " + trans_1 + " to transit to Line " + line1);
                print_stations(trans_2, trans_1, interm_line);
                direction_viewer(trans_1, end, direction_11, direction_12, starting_line);
                data.add("Here's you route in Line " + line1);
                print_stations(trans_1, end, starting_line);
            }
            return distance1 + distance2 + distance3;
        }
        return 0;
    }

    public void trans_station_path(int line_exists_1, int line_exists_2, int line_destination, String start, String end, String target_station, String trans_station_1, String trans_station_2, String trans_station_3, ArrayList<String> line_1,String station_11,String station_12, ArrayList<String> dest_line,String station_21,String station_22,ArrayList<String> line_3,String station_31,String station_32 ){
        if (start.equals(target_station)){


            int dist_min = shortest_route(transmition_viewer(line_exists_1, line_destination, start, end, trans_station_1, "Line "+line_exists_1, line_1, station_11, station_12, dest_line, station_21, station_22, false)
                    , transmition_viewer(line_exists_2, line_destination, start, end, trans_station_2, "Line "+line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, false)
                    , transmition_viewer(line_exists_2, line_destination, start, end, trans_station_3, "Line " + line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, false));
            if(dist_min == transmition_viewer(line_exists_1, line_destination, start, end, trans_station_1, "Line "+line_exists_1, line_1, station_11, station_12, dest_line, station_21, station_22, false)){
                data.add("Shortest route is: ");
                int distance_1 = transmition_viewer(line_exists_1, line_destination, start, end, trans_station_1, "Line "+line_exists_1, line_1, station_11, station_12, dest_line, station_21, station_22, true);
                if(distance_1 < 0)
                    distance_1*=-1;

            }
            else if(dist_min == transmition_viewer(line_exists_2, line_destination, start, end, trans_station_2, "Line "+line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, false)){
                data.add("Shortest route is: ");
                int distance_2 = transmition_viewer(line_exists_2, line_destination, start, end, trans_station_2, "Line "+line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, true);
                if(distance_2 < 0)
                    distance_2*=-1;


            }
            else if (dist_min == transmition_viewer(line_exists_2, line_destination, start, end, trans_station_3, "Line " + line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, false) && line_destination !=3) {
                data.add("Shortest route is: ");
                int distance_3 = transmition_viewer(line_exists_2, line_destination, start, end, trans_station_3, "Line " + line_exists_2, line_3, station_31, station_32, dest_line, station_21, station_22, true);
                if(distance_3 < 0)
                    distance_3*=-1;

            }


        }
        else if (end.equals(target_station)) {

            int dist_min = shortest_route(transmition_viewer(line_destination, line_exists_1, start, end, trans_station_1, "Line " + line_destination, dest_line, station_21, station_22, line_1, station_11, station_12, false)
                    , transmition_viewer(line_destination, line_exists_2, start, end, trans_station_2, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, false)
                    , transmition_viewer(line_destination, line_exists_2, start, end, trans_station_3, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, false));
            if(dist_min == transmition_viewer(line_destination, line_exists_1, start, end, trans_station_1, "Line " + line_destination, dest_line, station_21, station_22, line_1, station_11, station_12, false)){
                data.add("Shortest route is: ");
                int distance_1 = transmition_viewer(line_destination, line_exists_1, start, end, trans_station_1, "Line " + line_destination, dest_line, station_21, station_22, line_1, station_11, station_12, true);
                if(distance_1 < 0)
                    distance_1*=-1;


            }
            else if(dist_min == transmition_viewer(line_destination, line_exists_2, start, end, trans_station_2, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, false)){
                data.add("Shortest route is: ");
                int distance_2 = transmition_viewer(line_destination, line_exists_2, start, end, trans_station_2, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, true);
                if(distance_2 < 0)
                    distance_2*=-1;


            }
            else if (dist_min == transmition_viewer(line_destination, line_exists_2, start, end, trans_station_3, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, false) && line_destination !=3) {
                data.add("Shortest route is: ");
                int distance_3 = transmition_viewer(line_destination, line_exists_2, start, end, trans_station_3, "Line " + line_destination, dest_line, station_21, station_22, line_3, station_31, station_32, true);
                if(distance_3 < 0)
                    distance_3*=-1;

            }
        }
    }

}