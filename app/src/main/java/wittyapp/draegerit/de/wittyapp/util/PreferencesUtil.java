package wittyapp.draegerit.de.wittyapp.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public final class PreferencesUtil {

    private static final String PREFS_NAME = "wittyApp";
    private static final String IPADDRESS_PREF = "ipAddressPref";
    private static final String RGBCOLOR_PREF = "rgbColorPref";


    private static final String EMPTY = "";
    private static final int ZERO = 0;
    private static final int IN_DELAY = 3;

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

    public static String getIpAddress(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String json = settings.getString(IPADDRESS_PREF, EMPTY);
        if (!isBlank(json)) {
            Gson gson = new Gson();
            return gson.fromJson(json, String.class);
        }
        return null;
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

    private static String getDefaultRgbColorJson() {
        return new Gson().toJson(new RgbColor(0, 0, 0));
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().length() == ZERO;
    }


}
