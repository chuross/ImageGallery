package com.asomal.imagegallery.domain.image;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.NavigableMap;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.asomal.imagegallery.domain.dropbox.DropboxApiManager;
import com.asomal.imagegallery.util.DisplaySize;
import com.asomal.imagegallery.util.ImageUtil;
import com.asomal.imagegallery.util.Logger;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

/**
 * 画像をキャッシュする
 * 
 * @author chuross
 * 
 */
public class ImageCache {

	// メモリキャッシュする数
	private static final int MAX_CACHE = 100;

	private static final String TAG = ImageCache.class.getSimpleName();

	// サムネイルサイズ
	private static final float THUMBNAIL_DP = 100;

	private static NavigableMap<String, Bitmap> memCache = new TreeMap<String, Bitmap>();

	private static Context context;

	private static class ImageCacheHolder {
		private static ImageCache INSTANCE = new ImageCache();
	}

	/**
	 * インスタンスを取得する
	 * 
	 * @return {@link ImageCache}
	 */
	public static ImageCache getInstance(Context context) {
		ImageCache.context = context;
		return ImageCacheHolder.INSTANCE;
	}

	public Bitmap getImage(String filePath) {
		String fileName = new StringBuilder(filePath).substring(1);

		// メモリキャッシュから読み込む
		if (hasImage(fileName)) {
			Log.d(TAG, fileName + " read cache.");
			return memCache.get(fileName);
		}

		// ローカルファイルからの読み込み
		try {
			InputStream in = context.openFileInput(fileName);

			Log.d(TAG, fileName + " read local.");

			Bitmap image = ImageUtil.getThumbnail(DisplaySize.getInstance(context).pxToDip(THUMBNAIL_DP), in);
			setImage(fileName, image);

			return image;
		} catch (FileNotFoundException e) {
			Log.d(TAG, fileName + " read web.");
			// Dropboxからの読み込み
			BufferedInputStream bis = null;
			OutputStream out = null;
			try {
				// out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxApiManager(context).getApi();
				DropboxInputStream dropboxInputStream = dropboxApi.getFileStream(filePath, null);
				bis = new BufferedInputStream(dropboxInputStream);

				out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
				// 書き込み処理
				final byte[] buf = new byte[8 * 1024];
				int len = 0;

				while ((len = bis.read(buf)) != -1) {
					out.write(buf, 0, len);
				}

				bis = new
						BufferedInputStream(context.openFileInput(fileName));

				Bitmap image = ImageUtil.getThumbnail(DisplaySize.getInstance(context).pxToDip(THUMBNAIL_DP), bis);
				setImage(fileName, image);

				return image;
			} catch (DropboxException e1) {
				Logger.e(TAG, "getImage dropbox api error.", e1);
			} catch (FileNotFoundException e1) {
				Logger.e(TAG, "getImage file not found.", e1);
			} catch (IOException e1) {
				Logger.e(TAG, "getImage read error.", e1);
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e1) {
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e1) {
					}
				}
			}
		}

		return null;
	}

	/**
	 * メモリキャッシュに追加する
	 * 
	 * @param key キー
	 * @param image {@link Bitmap}
	 */
	public void setImage(String key, Bitmap image) {
		if (memCache.size() > MAX_CACHE) {
			memCache.remove(memCache.firstKey());
		}
		memCache.put(key, image);
	}

	/**
	 * メモリキャッシュされているか確認する
	 * 
	 * @param key キー
	 * @return true:有 false:無
	 */
	public boolean hasImage(String key) {
		return memCache.containsKey(key);
	}

	/**
	 * メモリキャッシュを空にする
	 */
	public void clear() {
		memCache.clear();
	}

}
