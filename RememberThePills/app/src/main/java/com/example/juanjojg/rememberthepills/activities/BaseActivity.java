package com.example.juanjojg.rememberthepills.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.example.juanjojg.rememberthepills.R;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    private static final String DARK_THEME_PREF = "dark_theme_preference";
    private static final String LANGUAGE_PREF = "language_preference";
    protected SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Get settings */
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        /* Theme must be applied before super.onCreate */
        applyTheme();
        /* Locale must be applied before super.onCreate */
        applyLanguage();
        super.onCreate(savedInstanceState);
    }

    private void applyTheme() {
        boolean enableDarkTheme = mSettings.getBoolean(DARK_THEME_PREF, false);
        if (enableDarkTheme) {
            setTheme(R.style.AppThemeDark);
        }
    }

    private void applyLanguage() {
        Locale locale = new Locale(mSettings.getString(LANGUAGE_PREF, "es"));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        Resources resources = getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
