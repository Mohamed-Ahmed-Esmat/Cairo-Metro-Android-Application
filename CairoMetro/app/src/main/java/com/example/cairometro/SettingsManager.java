package com.example.cairometro;
import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREF_NAME = "MySettings";
    private static final String KEY_SHAKE = "shakeState";
    private static final String KEY_COMFORT = "comfortState";
    private static final String KEY_LANG = "langChosen";

    private final SharedPreferences preferences;

    public SettingsManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean getShakeState() {
        return preferences.getBoolean(KEY_SHAKE, false);
    }

    public void setShakeState(boolean state) {
        preferences.edit().putBoolean(KEY_SHAKE, state).apply();
    }

    public boolean getComfortState() {
        return preferences.getBoolean(KEY_COMFORT, false);
    }

    public void setComfortState(boolean state) {
        preferences.edit().putBoolean(KEY_COMFORT, state).apply();
    }

    public String getLangChosen() {
        return preferences.getString(KEY_LANG, "English");
    }

    public void setLangChosen(String lang) {
        preferences.edit().putString(KEY_LANG, lang).apply();
    }
}

