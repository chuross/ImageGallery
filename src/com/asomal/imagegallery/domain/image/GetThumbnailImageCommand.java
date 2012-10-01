package com.asomal.imagegallery.domain.image;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;

import com.asomal.imagegallery.domain.dropbox.DropboxApiManager;
import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.util.DisplaySize;
import com.asomal.imagegallery.util.ImageUtil;
import com.asomal.imagegallery.util.Logger;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

public class GetThumbnailImageCommand implements Command<Bitmap> {

	private static final String TAG = GetThumbnailImageCommand.class.getSimpleName();

	String filePath;
	Context context;

	public GetThumbnailImageCommand(Context context, String filePath) {
		this.context = context;
		this.filePath = filePath;
	}

	@Override
	public Bitmap execute() {
		try {
			String fileName = new StringBuilder(filePath).substring(1);
			System.out.println("読み込み : " + fileName);
			OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxApiManager(context).getApi();
			dropboxApi.getFile(filePath, null, out, null);

			InputStream in = context.openFileInput(fileName);

			return ImageUtil.getThumbnail(DisplaySize.getInstance(context).pxToDip(100), in);
		} catch (DropboxException e) {
			Logger.e(TAG, "execute error.", e);
		} catch (FileNotFoundException e) {
			Logger.e(TAG, "execute file not found.", e);
		}

		return null;
	}
}
