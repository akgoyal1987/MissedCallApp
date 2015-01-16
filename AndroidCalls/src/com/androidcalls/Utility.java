package com.androidcalls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class Utility {
	public static final String SLIDEMENURBROADCAST = "Menuchange";

	private static String PREFERENCES = "dnd";

	public static void setBooleanPreferences(Context context, String key,
			boolean isCheck) {
		SharedPreferences setting = (SharedPreferences) context
				.getSharedPreferences(PREFERENCES, 0);

		SharedPreferences.Editor editor = setting.edit();

		editor.putBoolean(key, isCheck);
		editor.commit();

	}

	public static boolean getBooleanPreferences(Context context, String key) {

		SharedPreferences setting = (SharedPreferences) context
				.getSharedPreferences(PREFERENCES, 0);
		return setting.getBoolean(key, false);

	}

	public static void setStringPreferences(Context context, String key,
			String value) {
		SharedPreferences setting = (SharedPreferences) context
				.getSharedPreferences(PREFERENCES, 0);

		SharedPreferences.Editor editor = setting.edit();

		editor.putString(key, value);
		editor.commit();

	}

	public static String getStringPreferences(Context context, String key) {

		SharedPreferences setting = (SharedPreferences) context
				.getSharedPreferences(PREFERENCES, 0);
		return setting.getString(key, null);

	}

	public static void setIntegerPreferences(Context context, String key,
			int value) {
		SharedPreferences setting = (SharedPreferences) context
				.getSharedPreferences(PREFERENCES, 0);

		SharedPreferences.Editor editor = setting.edit();

		editor.putInt(key, value);
		editor.commit();

	}

	public static int getIntegerPreferences(Context context, String key) {

		SharedPreferences setting = (SharedPreferences) context
				.getSharedPreferences(PREFERENCES, 0);
		return setting.getInt(key, 0);

	}

	public static void showAlert(Context mContext, String title, String msg) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle(title);
		builder.setMessage(msg);
		// Set behavior of negative button

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});

		AlertDialog alert = builder.create();
		try {
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
