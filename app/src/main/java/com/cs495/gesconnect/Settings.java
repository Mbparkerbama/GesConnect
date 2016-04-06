package com.cs495.gesconnect;

import android.content.SharedPreferences;
import android.content.Context;
import com.cs495.gesconnect.GestureList;

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

    public static GestureList getGestureList() {
        return gestureList;
    }

    public static void saveGestureList(Context context) {
        setSetting(context,
                gestureListStorageName,
                gestureList.save());
    }

    public static void loadGestureList(Context context) {
        gestureList.load(getSetting(context,
                                gestureListStorageName,
                                ""));
    }

    static private GestureList gestureList;

    // String accessors for application settings
    final static public String vibrationEnabledString = "GesVibrationEnabled";

    // Predefined settings values
    final static public String trueString = "true";
    final static public String falseString = "false";

    // Name of the shared preference file the key/value pairs are stored to
    final static private String preferencesFileName = "GesConnectPref";

    final static private String gestureListStorageName = "GesMasterGestureList";
}
