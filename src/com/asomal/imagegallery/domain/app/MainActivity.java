package com.asomal.imagegallery.domain.app;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.GridView;

import com.asomal.imagegallery.R;
import com.asomal.imagegallery.domain.image.GetRemoteImageFilePathCommand;
import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.infrastructure.CommandExecuter;
import com.asomal.imagegallery.view.image.ImageGridAdapter;

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
				final ImageGridAdapter adapter = new ImageGridAdapter(MainActivity.this, result);
				GridView gridView = (GridView) findViewById(R.id.main_gridview);
				gridView.setAdapter(adapter);
				gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						adapter.clean(firstVisibleItem);
					}
				});
			}
		});
	}
}
