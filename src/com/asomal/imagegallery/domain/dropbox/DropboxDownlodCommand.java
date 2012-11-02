package com.asomal.imagegallery.domain.dropbox;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.util.Logger;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

/**
 * 画像をダウンロードしてくるクラス
 * 
 * @author chuross
 * 
 */
public class DropboxDownlodCommand implements Command<List<String>> {

	private static final String TAG = DropboxDownlodCommand.class.getSimpleName();
	Context context;
	List<String> pathList;
	Handler handler;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	TextView current;
	int i = 0;

	public DropboxDownlodCommand(Context context, List<String> pathList) {
		this.context = context;
		this.pathList = pathList;
		handler = new Handler();
	}

	@Override
	public List<String> execute() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				builder = new AlertDialog.Builder((Activity) context);
				LinearLayout viewGroup = new LinearLayout(context);
				current = new TextView(context);
				current.setTextSize(25f);
				viewGroup.addView(current);
				builder.setView(viewGroup);
				builder.setMessage("Dropboxからダウンロードしています。");
				builder.setCancelable(false);
				dialog = builder.show();
			}
		});

		for (String path : pathList) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					if (current == null) {
						return;
					}
					current.setText(i + "/" + pathList.size());
				}
			});
			String fileName = new StringBuilder(path).substring(1);
			try {
				context.openFileInput(fileName);
				Log.d(TAG, fileName + " already saved.");
			} catch (FileNotFoundException e) {
				Log.d(TAG, fileName + " read start.");
				try {
					OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
					DropboxAPI<AndroidAuthSession> dropboxApi = new DropboxApiManager(context).getApi();
					dropboxApi.getFile(path, null, out, null);
				} catch (FileNotFoundException e1) {
					Logger.e(TAG, "dounload path not found.", e1);
				} catch (DropboxException e1) {
					Logger.e(TAG, "dounload save error.", e1);
				}
			}

			i++;
		}
		dialog.dismiss();
		return pathList;
	}

	@Override
	public void onCanceled() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
