package com.asomal.imagegallery.util;

import android.util.Log;

/**
 * ログ出力用
 * 
 * @author chuross
 * 
 */
public class Logger {

	public static void e(String tag, String msg, Throwable e) {
		Log.e(tag, msg, e);
	}

	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static void w(String tag, String msg) {
		Log.w(tag, msg);
	}

}
