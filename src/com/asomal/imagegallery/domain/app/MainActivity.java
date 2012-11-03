package com.asomal.imagegallery.domain.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
			public void onFinished(List<String> filePathList) {
				if (filePathList.size() <= 0) {
					Toast.makeText(MainActivity.this, "ネットワークに接続されていない可能性があります", Toast.LENGTH_LONG).show();
					return;
				}

				setGridView(filePathList);
			}
		});
	}

	/**
	 * @param filePathList ファイルパスリスト
	 */
	public void setGridView(final List<String> filePathList) {
		final ImageGridAdapter adapter = new ImageGridAdapter(MainActivity.this, filePathList);

		final GridView gridView = (GridView) findViewById(R.id.main_gridview);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(DetailActivity
						.createIntent(MainActivity.this, position, (ArrayList<String>) filePathList));
			}
		});
		gridView.setOnScrollListener(new
				AbsListView.OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view, int
							scrollState) {
						switch (scrollState) {
							case OnScrollListener.SCROLL_STATE_IDLE:
								adapter.clean(gridView.getFirstVisiblePosition(),
										gridView.getLastVisiblePosition());
								break;
							case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
								break;
							case OnScrollListener.SCROLL_STATE_FLING:
								break;
						}
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount,
							int totalItemCount) {
					}
				});
	}
}
