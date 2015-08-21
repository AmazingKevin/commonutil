package cn.ibona.commonutil;


import android.content.Context;
import android.content.SharedPreferences;


/**
 * init before use.
 */
public final class BaseSettings {
	BaseSettings(){}

	static SharedPreferences prefer = null;

	public static void init(Context ctx) {
		if(prefer == null) {
			prefer = ctx.getSharedPreferences("esmoke_prefer", Context.MODE_PRIVATE);
		}
	}

	public static String getString(String key, String defValue) {
		String re= prefer.getString(key, defValue);
		return re;
	}
	
	public static int getInt(String key, int defValue) {
		int re= prefer.getInt(key, defValue);
		return re;
	}

	public static boolean getBoolean(String key, boolean defValue) {
		boolean re= prefer.getBoolean(key, defValue);
		return re;
	}
	
	public static long getLong(String key, long defValue) {
		long re= prefer.getLong(key, defValue);
		return re;
	}
	
	public static float getFloat(String key, float defValue){
		float re = prefer.getFloat(key, defValue);
		return re;
	}

	public static void putString(String key, String value) {
		prefer.edit().putString(key, value).commit();
	}

	public static void putInt(String key, int value) {
		prefer.edit().putInt(key, value).commit();
	}

	public static void putBoolean(String key, boolean value) {
		prefer.edit().putBoolean(key, value).commit();
	}
	
	public static void putLong(String key, long value) {
		prefer.edit().putLong(key, value).commit();
	}
	
	public static void putFloat(String key, float value){
		prefer.edit().putFloat(key, value).commit();
	}
	
}
