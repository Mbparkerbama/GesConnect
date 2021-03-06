package com.cs495.gesconnect;

import android.content.SharedPreferences;
import android.content.Context;
import android.util.Log;

import com.cs495.gesconnect.GestureList;

import java.io.IOException;

public class Settings {
    public static String getSetting(Context context, String key) {
        // If the key doesn't exist, return an empty string
        return getSetting(context, key, "");
    }

    public static String getSetting(Context context, String key, String defaultVal) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(preferencesFileName,
                Context.MODE_PRIVATE);

        Log.d(TAG, "Reading " + key + ":" + sharedPreferences.getString(key, defaultVal));
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
        Log.d(TAG, "Saved " + key + ":" + value);
    }

    public static GestureList getGestureList() {
        return gestureList;
    }

    public static void saveGestureList(Context context) {
        Log.d(TAG, "Saving Gesture List");
        setSetting(context,
                gestureListStorageName,
                gestureList.save());
    }

    public static void loadGestureList(Context context) {
        String loadData = getSetting(context,
                gestureListStorageName,
                "");
        gestureList.load(loadData);
    }

    static private GestureList gestureList = new GestureList();

    // String accessors for application settings
    final static public String vibrationEnabledString = "GesVibrationEnabled";

    // Predefined settings values
    final static public String trueString = "true";
    final static public String falseString = "false";

    // Name of the shared preference file the key/value pairs are stored to
    final static private String preferencesFileName = "GesConnectPref";

    final static private String gestureListStorageName = "GesMasterGestureList";

    private static final String TAG = "Settings";
}
