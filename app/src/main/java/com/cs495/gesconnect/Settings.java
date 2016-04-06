package com.cs495.gesconnect;

import android.content.SharedPreferences;
import android.content.Context;

public class Settings {
    public static String getSetting(Context context, String key) {
        // If the key doesn't exist, return an empty string
        return getSetting(context, key, "");
    }

    public static String getSetting(Context context, String key, String defaultVal) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(preferencesFileName,
                Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, defaultVal);
    }

    public static void setSetting(Context context, String key, String value) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(preferencesFileName,
                0);

        SharedPreferences.Editor editor
            = sharedPreferences.edit();

        editor.putString(key, value);
        editor.commit();
    }

/*    public static GestureList getGestureList() {

    } */

    // String accessors for application settings
    final static public String vibrationEnabledString = "GesVibrationEnabled";

    // Predefined settings values
    final static public String trueString = "true";
    final static public String falseString = "false";

    // Name of the shared preference file the key/value pairs are stored to
    final static protected String preferencesFileName = "GesConnectPref";
}
