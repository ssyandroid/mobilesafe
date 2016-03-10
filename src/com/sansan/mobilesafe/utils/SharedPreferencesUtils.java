package com.sansan.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

	private static SharedPreferences sp = null;
	private String SP_NAME = "config";

	// SharedPreferences sp = ct.getSharedPreferences("config",
	// ct.MODE_PRIVATE);
	// Editor edit = sp.edit();
	// edit.putString("XXXX", responseInfo.result);
	// edit.commit();

	public SharedPreferencesUtils(Context ct) {
		super();
		sp = ct.getSharedPreferences(SP_NAME, 0);
	}

	public int getInt(String key) {
		return sp.getInt(key, 0);
	}
	public String getString(String key) {
		return sp.getString(key, "");
	}

	public Boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public void saveInt(String key, int i) {
		sp.edit().putInt(key, i).commit();
	}
	public void saveString(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	public void saveBoolean(String key, Boolean b) {
		sp.edit().putBoolean(key, b).commit();
	}
}
