package com.asomal.imagegallery.domain.app;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import com.asomal.imagegallery.R;
import com.asomal.imagegallery.domain.image.GetRemoteImageFilePathCommand;
import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.infrastructure.CommandExecuter;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		Resources res = getResources();
		SharedPreferences sp = getSharedPreferences(res.getString(R.string.sp_dropbox_auth), MODE_PRIVATE);

		// 初回起動
		if (!sp.getBoolean(res.getString(R.string.sp_key_is_autentication), false)) {
			startActivity(new Intent(this, DropboxAuthActivity.class));
			finish();
			return;
		}

		CommandExecuter.post(new GetRemoteImageFilePathCommand(this), new Command.OnFinishListener<List<String>>() {

			@Override
			public void onFinished(List<String> result) {
			}
		});
	}
}
