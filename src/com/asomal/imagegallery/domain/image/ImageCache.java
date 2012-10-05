package com.asomal.imagegallery.domain.image;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.NavigableMap;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.Bitmap;

import com.asomal.imagegallery.domain.dropbox.DropboxApiManager;
import com.asomal.imagegallery.util.DisplaySize;
import com.asomal.imagegallery.util.ImageUtil;
import com.asomal.imagegallery.util.Logger;
import com.dropbox.client2.DropboxAPI;
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
	private static final int MAX_CACHE = 150;

	private static final String TAG = ImageCache.class.getSimpleName();

	// サムネイルサイズ
	private static final float THUMBNAIL_DP = 100;

	private static NavigableMap<String, Bitmap> memCache = new TreeMap<String, Bitmap>();

	private static Context staticContext;

	private static class ImageCacheHolder {
		private static ImageCache INSTANCE = new ImageCache();
	}

	/**
	 * インスタンスを取得する
	 * 
	 * @return {@link ImageCache}
	 */
	public static ImageCache getInstance(Context context) {
		staticContext = context;
		return ImageCacheHolder.INSTANCE;
	}

	public Bitmap getImage(String filePath) {
		String fileName = new StringBuilder(filePath).substring(1);

		// メモリキャッシュから読み込む
		if (hasImage(fileName)) {
			return memCache.get(fileName);
		}

		// ローカルファイルからの読み込み
		try {
			InputStream in = staticContext.openFileInput(fileName);

			Bitmap image = ImageUtil.getThumbnail(DisplaySize.getInstance(staticContext).pxToDip(THUMBNAIL_DP), in);
			setImage(fileName, image);

			return image;
		} catch (FileNotFoundException e) {
			// Dropboxからの読み込み
			try {
				OutputStream out = staticContext.openFileOutput(fileName, Context.MODE_PRIVATE);
				DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxApiManager(staticContext).getApi();
				dropboxApi.getFile(filePath, null, out, null);

				InputStream in = staticContext.openFileInput(fileName);

				Bitmap image = ImageUtil.getThumbnail(DisplaySize.getInstance(staticContext).pxToDip(THUMBNAIL_DP), in);
				setImage(fileName, image);

				return image;
			} catch (FileNotFoundException e1) {
				Logger.e(TAG, "getImage error.", e1);
			} catch (DropboxException e1) {
				Logger.e(TAG, "getImage dropbox api error.", e1);
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
