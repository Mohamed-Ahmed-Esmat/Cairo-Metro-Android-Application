package com.example.cairometro;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    Switch shakeSwitch;
    Switch comfortSwitch;
    Spinner languageSpinner;
    SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        shakeSwitch = findViewById(R.id.shakeSwitch);
        comfortSwitch = findViewById(R.id.comfortSwitch);
        languageSpinner = findViewById(R.id.languageSpinner);

        settingsManager = new SettingsManager(this);

        shakeSwitch.setChecked(settingsManager.getShakeState());
        comfortSwitch.setChecked(settingsManager.getComfortState());

        String[] languages = {"Arabic", "English"};
        String langChosen = settingsManager.getLangChosen();
        int langIndex = langChosen.equals("Arabic") ? 0 : 1;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        languageSpinner.setAdapter(adapter);
        languageSpinner.setSelection(langIndex);
    }

    @Override
    public void onBackPressed() {
        boolean shakeState = shakeSwitch.isChecked();
        boolean comfortState = comfortSwitch.isChecked();
        String langChosen = languageSpinner.getSelectedItem().toString();

        settingsManager.setShakeState(shakeState);
        settingsManager.setComfortState(comfortState);
        settingsManager.setLangChosen(langChosen);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("shakeState", shakeState);
        intent.putExtra("comfortState", comfortState);
        intent.putExtra("langChosen", langChosen);

        // Clear the activity stack and start the MainActivity as a new task
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }





    public void shakey(View view) {
        System.out.println("The status is ");
    }

    public void comfort(View view) {

    }

    public void lang(View view) {
    }
}