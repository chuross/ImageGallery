package com.asomal.imagegallery.domain.image;

import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;

import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.util.Logger;

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
		return ImageCache.getInstance(context).getImage(filePath);
	}

	@Override
	public void onCanceled() {
		String fileName = new StringBuilder(filePath).substring(1);
		try {
			if (context.openFileInput(fileName) != null) {
				return;
			}

			context.deleteFile(fileName);
		} catch (FileNotFoundException e) {
			Logger.d(TAG, "Cancel file is not created.");
		}
	}
}
