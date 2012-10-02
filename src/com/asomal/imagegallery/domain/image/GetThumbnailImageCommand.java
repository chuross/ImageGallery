package com.asomal.imagegallery.domain.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.asomal.imagegallery.infrastructure.Command;

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
}
