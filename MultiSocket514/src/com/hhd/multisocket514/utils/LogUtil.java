package com.hhd.multisocket514.utils;

import android.util.Log;

public class LogUtil {
	
	private static final String TAG = "MutilSocket";

	public static void i(String msg){
		Log.i(TAG, msg);
	}
	public static void e(String msg){
		Log.e(TAG, msg);
	}
	public static void w(String msg){
		Log.w(TAG, msg);
	}
	public static void d(String msg){
		Log.d(TAG, msg);
	}

}
