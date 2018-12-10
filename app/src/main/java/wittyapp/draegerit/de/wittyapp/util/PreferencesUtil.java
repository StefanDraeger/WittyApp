package wittyapp.draegerit.de.wittyapp.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public final class PreferencesUtil {

    private static final String PREFS_NAME = "wittyApp";
    private static final String IPADDRESS_PREF = "ipAddressPref";
    private static final String RGBCOLOR_PREF = "rgbColorPref";
    private static final String BUZZER_PREF = "buzzerPref";

    private static final String CONFIG_PREF = "configPref";


    private static final String EMPTY = "";
    private static final int ZERO = 0;
    private static final int IN_DELAY = 3;
    private static String defaultBuzzerJson;

    private PreferencesUtil() {

    }

    public static void storeIpAddress(Context ctx, String ipAddress) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ipAddress);
        editor.putString(IPADDRESS_PREF, json);
        editor.apply();
    }

    public static void storeRgbColor(Context ctx, RgbColor rgbColor) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rgbColor);
        editor.putString(RGBCOLOR_PREF, json);
        editor.apply();
    }

    public static void storeBuzzerValue(Context ctx, BuzzerValue buzzerValue) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(buzzerValue);
        editor.putString(BUZZER_PREF, json);
        editor.apply();
    }

    public static String getIpAddress(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String json = settings.getString(IPADDRESS_PREF, EMPTY);
        if (!isBlank(json)) {
            Gson gson = new Gson();
            return gson.fromJson(json, String.class);
        }
        return null;
    }

    public static Configuration getConfiguration(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String json = settings.getString(CONFIG_PREF, EMPTY);
        if (!isBlank(json)) {
            Gson gson = new Gson();
            return gson.fromJson(json, Configuration.class);
        }
        return new Configuration();
    }

    public static void storeConfiguration(Context ctx, Configuration configuration) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(configuration);
        editor.putString(CONFIG_PREF, json);
        editor.apply();
    }

    public static RgbColor getRgbColor(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String json = settings.getString(RGBCOLOR_PREF, getDefaultRgbColorJson());
        if (!isBlank(json)) {
            Gson gson = new Gson();
            return gson.fromJson(json, RgbColor.class);
        }
        return null;
    }

    public static BuzzerValue getBuzzerValue(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String json = settings.getString(BUZZER_PREF, getDefaultBuzzerJson());
        if (!isBlank(json)) {
            Gson gson = new Gson();
            return gson.fromJson(json, BuzzerValue.class);
        }
        return null;
    }

    private static String getDefaultRgbColorJson() {
        return new Gson().toJson(new RgbColor(0, 0, 0));
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().length() == ZERO;
    }

    public static String getDefaultBuzzerJson() {
        return new Gson().toJson(new BuzzerValue(31, 1000));
    }
}
