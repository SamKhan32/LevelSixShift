package com.example.levelsixshift;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserDataManager {
    private static final String PREF_NAME = "YourPreferenceName";
    private static SharedPreferences sharedPreferences;


    private UserDataManager() {
        // Private constructor to prevent instantiation
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void saveData(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void editData(Context context, String key, String value) {

    }

    public static String loadData(Context context, String key, String defaultValue) {
        if(getSharedPreferences(context).contains(key)) {
            Log.i("ButtonClick", "Default Value triggered");
            Log.i("ButtonClick", "Retrieving saved value triggered");
            return getSharedPreferences(context).getString(key, defaultValue);
        }
        Log.i("ButtonClick", "Default Value triggered");
        return defaultValue;
    }
}