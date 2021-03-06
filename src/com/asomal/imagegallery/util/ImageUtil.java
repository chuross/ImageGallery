package com.asomal.imagegallery.util;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 画像であんなことこんなこと
 * 
 * @author chuross
 * 
 */
public class ImageUtil {

	private static final String TAG = ImageUtil.class.getSimpleName();

	/**
	 * 詳細画像を取得する
	 * 
	 * @param requestWidth 幅
	 * @param requestHeight 高さ
	 * @param inputStream {@link InputStream}
	 * @return {@link Bitmap}
	 */
	public static Bitmap getDetail(float requestWidth, float requestHeight, InputStream inputStream) {
		if (requestWidth <= 0 || requestHeight <= 0) {
			return null;
		}

		Bitmap src = null;
		try {
			BitmapFactory.Options bmpFactoryOpt = new BitmapFactory.Options();
			bmpFactoryOpt.inJustDecodeBounds = false;
			bmpFactoryOpt.inSampleSize = 2;

			src = BitmapFactory.decodeStream(inputStream, null, bmpFactoryOpt);

			if (src == null) {
				Log.e(TAG, "getDetail src is null.");
				return null;
			}

			int srcWidth = src.getWidth();
			int srcHeight = src.getHeight();

			float wScale = srcWidth / requestWidth;
			float hScale = srcHeight / requestHeight;
			float scale = Math.max(wScale, hScale);

			int resizeWidth = (int) Math.ceil(srcWidth / scale);
			int resizeHeight = (int) Math.ceil(srcHeight / scale);

			return Bitmap.createScaledBitmap(src, resizeWidth, resizeHeight, false);
		} finally {
			if (src != null) {
				src.recycle();
				src = null;
			}
		}
	}

	/**
	 * 画像のサムネイルを取得
	 * 
	 * @param requestSize 生成する画像サイズ
	 * @param inputStream 処理したい画像の{@link InputStream}
	 * @return 正方形の{@link Bitmap}
	 */
	public static Bitmap getThumbnail(float requestSize, InputStream inputStream) {
		if (requestSize <= 0) {
			return null;
		}

		Bitmap src = null;
		Bitmap resizeBmp = null;
		try {
			BitmapFactory.Options bmpFactoryOpt = new BitmapFactory.Options();
			bmpFactoryOpt.inJustDecodeBounds = false;
			bmpFactoryOpt.inSampleSize = 4;

			src = BitmapFactory.decodeStream(inputStream, null, bmpFactoryOpt);

			if (src == null) {
				Log.e(TAG, "getThumbnail() src is null.");
				return null;
			}

			int srcWidth = src.getWidth();
			int srcHeight = src.getHeight();

			float targetSize = Math.min(srcWidth, srcHeight);

			float scale = targetSize / requestSize;

			int resizeWidth = (int) Math.ceil(srcWidth / scale);
			int resizeHeight = (int) Math.ceil(srcHeight / scale);

			if (resizeWidth > resizeHeight) {
				resizeBmp = Bitmap.createScaledBitmap(src, resizeWidth, resizeHeight, false);
				return Bitmap.createBitmap(resizeBmp, 0, 0, (int) requestSize, (int) requestSize);
			} else {
				resizeBmp = Bitmap.createScaledBitmap(src, resizeWidth, resizeHeight, false);
				return Bitmap.createBitmap(resizeBmp, 0, 0, (int) requestSize, (int) requestSize);
			}
		} finally {
			if (src != null) {
				src.recycle();
				src = null;
			}
			if (resizeBmp != null) {
				resizeBmp.recycle();
				resizeBmp = null;
			}
		}
	}

}
