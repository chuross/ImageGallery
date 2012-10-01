package com.asomal.imagegallery.domain.image;

import android.graphics.Bitmap;

import com.asomal.imagegallery.infrastructure.Command;

public class GetThumbnailImageCommand implements Command<Bitmap> {

	String filePath;

	public GetThumbnailImageCommand(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public Bitmap execute() {
		return null;
	}

}
