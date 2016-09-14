package com.yetwish.contactsdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yetwish.contactsdemo.BaseApplication;

import java.util.Map;

/**
 * SharedPreferences Utils
 * Created by yetwish on 2016/9/14.
 */
public class SPUtils {

    private static final String SP_NAME = "contactsPreferences";

    public static final String SP_FIRST_START = "firstStartApp";

    private static SPUtils sInstance;

    private SharedPreferences mPreferences;


    private SPUtils() {
        mPreferences = BaseApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static SPUtils getInstance() {
        if (sInstance == null) {
            synchronized (SPUtils.class) {
                if (sInstance == null)
                    sInstance = new SPUtils();
            }
        }
        return sInstance;
    }


    public void put(String key, Object value) {

        SharedPreferences.Editor editor = mPreferences.edit();

        if (value instanceof String)
            editor.putString(key, (String) value);
        else if (value instanceof Boolean)
            editor.putBoolean(key, (Boolean) value);
        else if (value instanceof Float)
            editor.putFloat(key, (Float) value);
        else if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else if (value instanceof Long)
            editor.putLong(key, (Long) value);
        else
            editor.putString(key, value.toString());
        editor.apply();
    }

    public Object get(String key, Object defaultValue) {

        if (defaultValue instanceof String)
            return mPreferences.getString(key, (String) defaultValue);
        else if (defaultValue instanceof Boolean)
            return mPreferences.getBoolean(key, (Boolean) defaultValue);
        else if (defaultValue instanceof Float)
            return mPreferences.getFloat(key, (Float) defaultValue);
        else if (defaultValue instanceof Integer)
            return mPreferences.getInt(key, (Integer) defaultValue);
        else if (defaultValue instanceof Long)
            return mPreferences.getLong(key, (Long) defaultValue);

        return null;
    }


    public boolean contains(String key) {
        return mPreferences.contains(key);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public Map<String, ?> getAll() {
        return mPreferences.getAll();
    }

}
