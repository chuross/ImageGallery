package com.asomal.imagegallery.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplaySize {

	private static Context staticContext;
	private DisplayMetrics metrics;

	private DisplaySize(Context context) {
		metrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
	}

	// Singleton Holder
	private static class DisplaySizeHolder {

		private final static DisplaySize INSTANCE = new DisplaySize(staticContext);

	}

	/**
	 * インスタンスを取得
	 * 
	 * @param context {@link Context}
	 * @return インスタンス
	 */
	public static DisplaySize getInstance(Context context) {
		staticContext = context;
		return DisplaySizeHolder.INSTANCE;
	}

	/**
	 * 画面の幅を取得する
	 * 
	 * @return 画面の幅
	 */
	public float getDisplayWidth() {
		return metrics.widthPixels;
	}

	/**
	 * 画面の高さを取得する
	 * 
	 * @return 画面の高さ
	 */
	public float getDisplayHeight() {
		return metrics.heightPixels;
	}

	/**
	 * ピクセルをDPに変換する
	 * 
	 * @param px 画像px
	 * @return DPに変換された値
	 */
	public float pxToDip(float px) {
		return metrics.scaledDensity * px;
	}
}
