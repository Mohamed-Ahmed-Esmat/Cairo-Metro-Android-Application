package com.example.cairometro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    Switch shakeSwitch;
    Switch comfortSwitch;
    Spinner languageSpinner;
    boolean shakeState, lastShake;
    boolean comfortState, lastComfort;
    String langChose="English", lastLang, transitLang;
    String [] languages= {"Arabic", "English"};
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        shakeSwitch = findViewById(R.id.shakeSwitch);
        comfortSwitch = findViewById(R.id.comfortSwitch);
        languageSpinner = findViewById(R.id.languageSpinner);
        pref = getPreferences(MODE_PRIVATE);
        lastShake = pref.getBoolean("shakeState", false);
        lastComfort = pref.getBoolean("comfortState", false);
        lastLang = pref.getString("langChosen", "");

        shakeSwitch.setChecked(lastShake);
        comfortSwitch.setChecked(lastComfort);

        if(languages[0].equals(lastLang))
            languageSpinner.setSelection(0);
        else{
            transitLang = languages[0];
            languages[0]=lastLang;
            languages[1] = transitLang;
            languageSpinner.setSelection(0);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        languageSpinner.setAdapter(adapter);
        System.out.println("Shake status of settings = "+shakeState);
        System.out.println("Shake status of previous page = "+lastShake);
    }

    @Override
    public void onBackPressed() {
        langChose = languageSpinner.getSelectedItem().toString();

        shakeState = shakeSwitch.isChecked();
        comfortState = comfortSwitch.isChecked();
        SharedPreferences.Editor e = pref.edit();
        e.putBoolean("shakeState", shakeState);
        e.putBoolean("comfortState", comfortState);
        e.putString("langChosen", langChose);
        e.apply();
        Intent a = new Intent(this, MainActivity.class);
        a.putExtra("shakeState", shakeState);
        a.putExtra("comfortState", comfortState);
        a.putExtra("langChosen", langChose);
        startActivity(a);
        super.onBackPressed();
    }

    public void shakey(View view) {
        System.out.println("The status is "+shakeState);
    }

    public void comfort(View view) {

    }

    public void lang(View view) {
    }
}