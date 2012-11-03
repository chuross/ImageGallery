package com.asomal.imagegallery.domain.image;

import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;

import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.util.Logger;

/**
 * 詳細画像を取得する
 * 
 * @author chuross
 * 
 */
public class GetDetailImageCommand implements Command<Bitmap> {

	private static final String TAG = GetDetailImageCommand.class.getSimpleName();
	Context context;
	String path;
	private ImageCache cache;

	public GetDetailImageCommand(Context context, String path, ImageCache cache) {
		this.context = context;
		this.path = path;
		this.cache = cache;
	}

	@Override
	public Bitmap execute() {
		return cache.getImage(path);
	}

	@Override
	public void onCanceled() {
		String fileName = new StringBuilder(path).substring(1);
		try {
			if (context.openFileInput(fileName) != null) {
				return;
			}

			// ファイルがキャンセルなどで中途半端に書き込まれているとnullになる？ので一旦消す
			context.deleteFile(fileName);
		} catch (FileNotFoundException e) {
			Logger.d(TAG, "Cancel file is not created.");
		}
	}

}
