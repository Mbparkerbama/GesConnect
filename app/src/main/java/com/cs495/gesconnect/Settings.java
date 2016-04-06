package com.cs495.gesconnect;

import android.content.SharedPreferences;
import android.content.Context;

public class Settings {
    public static String getSetting(Context context, String key) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(preferencesFileName,
                                                0);

        // If the key doesn't exist, return a null string
        return sharedPreferences.getString(key, "");
    }

    public static void setSetting(Context context, String key, String value) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(preferencesFileName,
                0);

        SharedPreferences.Editor editor
            = sharedPreferences.edit();

        editor.putString(key, value);
    }

/*    public static GestureList getGestureList() {

    } */

    final static protected String preferencesFileName = "GesConnectPref";
}
