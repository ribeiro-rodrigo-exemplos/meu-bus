package br.com.hhw.startapp.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Build.VERSION;

public class SharedPreferencesHelper {

	static String PREFERENCES_NAME;

	public static String KEY_FIRST_LAUCH = "first lauch";
	public static String KEY_TOKEN = "token";
	public static String KEY_PHONE = "phonenumber";
	
	public static String KEY_SHOW_TUTORIAL = "tutorial seja treinador";

	Context context;

	SharedPreferences sharedPreferences;

	private static SharedPreferencesHelper instance = null;

	public SharedPreferencesHelper(Context context) {
		this.context = context;

		PREFERENCES_NAME = this.getPackageName();

		sharedPreferences = this.context.getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_PRIVATE);

		// In later versions multi_process is no longer the default
		if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			sharedPreferences = getSharedPreferences(context);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) public SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_MULTI_PROCESS);
	}

	public static SharedPreferencesHelper getInstance(Context context) {
		if (instance == null) {
			instance = new SharedPreferencesHelper(context);
		}

		return instance;
	}

	public String getPackageName() {
		return this.context.getApplicationContext().getPackageName();
	}

	public int getInt(String key, int defaultValue) {
		return this.sharedPreferences.getInt(key, defaultValue);
	}

	public int getInt(String key) {
		return this.getInt(key, 0);
	}

	public boolean setInt(String key, int value) {
		Editor edit = this.sharedPreferences.edit();
		edit.putInt(key, value);
		return edit.commit();
	}

	public String getString(String key, String defaultValue) {
		return this.sharedPreferences.getString(key, defaultValue);
	}

	public String getString(String key) {
		return this.getString(key, null);
	}

	public boolean setString(String key, String value) {
		Editor edit = this.sharedPreferences.edit();
		edit.putString(key, value);
		return edit.commit();
	}

	public boolean getBoolean(String key) {
		return this.sharedPreferences.getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return this.sharedPreferences.getBoolean(key, defaultValue);
	}

	public boolean setBoolean(String key, boolean value) {
		Editor edit = this.sharedPreferences.edit();
		edit.putBoolean(key, value);
		return edit.commit();
	}

	public boolean setFloat(String key, float value) {
		Editor edit = this.sharedPreferences.edit();
		edit.putFloat(key, value);
		return edit.commit();
	}

	public float getFloat(String key, float defaultValue) {
		return this.sharedPreferences.getFloat(key, defaultValue);
	}

	public boolean isFirstLauch() {
		return this.sharedPreferences.getBoolean(KEY_FIRST_LAUCH, true);
	}

	public String getToken() {
		return this.sharedPreferences.getString(KEY_TOKEN, null);
	}

	public void setPhone(String phone) {
		this.setString(KEY_PHONE, phone);
	}

	public String getPhone() {
		return this.sharedPreferences.getString(KEY_PHONE, "");
	}

	public void setHasToShowTutorial(boolean flag) {
		this.setBoolean(KEY_SHOW_TUTORIAL, flag);
	}

	public boolean hasToShowTutorial() {
		return this.sharedPreferences.getBoolean(
				KEY_SHOW_TUTORIAL, true);
	}
}
