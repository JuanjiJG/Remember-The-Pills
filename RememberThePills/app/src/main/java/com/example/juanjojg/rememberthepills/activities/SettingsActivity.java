package com.example.juanjojg.rememberthepills.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.juanjojg.rememberthepills.fragments.SettingsFragment;

import java.util.Locale;

public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final int RESULT_CODE_DARK_THEME = 1;
    private static final String TAG = "SettingsActivity";
    private static final String DARK_THEME_PREF = "dark_theme_preference";
    private static final String LANGUAGE_PREF = "language_preference";

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new SettingsFragment())
                    .commit();
        }

        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onResume() {
        Log.i(TAG, "Resuming activity");
        super.onResume();
        this.preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        Log.i(TAG, "Pausing activity");
        super.onPause();
        this.preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(TAG, "SharedPreference changed");
        Log.i(TAG, "The changed preference is: " + key);
        if (key.equals(DARK_THEME_PREF)) {
            Log.i(TAG, "Dark theme preference changed");
            setResult(RESULT_CODE_DARK_THEME);
            recreate();
        }
        else if(key.equals(LANGUAGE_PREF)) {
            Locale locale = new Locale(preferences.getString(LANGUAGE_PREF, "es"));
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            Resources resources = getResources();
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            setResult(RESULT_CODE_DARK_THEME);
            recreate();
        }
    }
}
