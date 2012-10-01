package com.asomal.imagegallery.domain.image;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.asomal.imagegallery.domain.dropbox.DropboxApiManager;
import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.util.Logger;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

/**
 * リモートにある画像パスを取得する
 * 
 * @author chuross
 * 
 */
public class GetRemoteImageFilePathCommand implements Command<List<String>> {

	private static final String TAG = GetRemoteImageFilePathCommand.class.getSimpleName();

	Context context;

	public GetRemoteImageFilePathCommand(Context context) {
		this.context = context;
	}

	@Override
	public List<String> execute() {
		List<String> pathList = new ArrayList<String>();
		try {
			DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxApiManager(context).getApi();
			Entry dirEntry = dropboxApi.metadata("/", 20, null, true, null);

			List<Entry> fileList = dirEntry.contents;
			for (Entry entry : fileList) {
				pathList.add(entry.path);
			}
		} catch (DropboxException e) {
			Logger.e(TAG, "execute error.", e);
		}

		return pathList;
	}
}
