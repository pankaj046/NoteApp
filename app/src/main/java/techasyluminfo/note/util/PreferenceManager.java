package techasyluminfo.note.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public final class PreferenceManager {

    public static final String PREFERENCE_NAME = "note";
    Context ctx;

    private PreferenceManager() {
    }

    private static SharedPreferences getPreferences(Context context) {
        if (context != null) {
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return null;
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null && preferences.contains(key)) {
            return preferences.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public static void saveString(Context context, String key, String value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            preferences.edit().putString(key, value).apply();
        }
    }

    public static boolean getBoolean(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null && preferences.contains(key)) {
            return preferences.getBoolean(key, false);
        }
        return false;
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            preferences.edit().putBoolean(key, value).apply();
        }
    }

    public static long getLong(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        try {
            return preferences.getLong(key, 0);
        } catch (Exception e) {
        }
        return 0;
    }


    public static double getdouble(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        try {
            return Double.parseDouble(preferences.getString(key, ""));
        } catch (Exception e) {
        }
        return 0;
    }


    public static void savedouble(Context context, String key, double value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            try {
                preferences.edit().putString(key, String.valueOf(value)).apply();
            } catch (Exception e) {

            }
        }
    }

    public static void saveLong(Context context, String key, long value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            try {
                preferences.edit().putLong(key, value).apply();
            } catch (Exception e) {

            }
        }
    }

    public static float getFloat(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null && preferences.contains(key)) {
            return preferences.getFloat(key, 0);
        }
        return 0;
    }

    public static void saveFloat(Context context, String key, float value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            preferences.edit().putFloat(key, value).apply();
        }
    }
    public static void deleteAllData(Context context) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            preferences.edit().clear().apply();
        }
    }

    public static void delete(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            preferences.edit().remove(key).apply();
        }
    }

    public static int getInt(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        try {
            return preferences.getInt(key, 0);
        } catch (Exception e) {
            Log.e("TAG", "getInt: ");
        }
        return 0;
    }

    public static void saveInt(Context context, String key, int value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            try {
                preferences.edit().putInt(key, value).apply();
            } catch (Exception e) {

            }
        }
    }
}
