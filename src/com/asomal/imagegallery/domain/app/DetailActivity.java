package com.asomal.imagegallery.domain.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.asomal.imagegallery.R;
import com.asomal.imagegallery.view.image.ImagePagerAdapter;

/**
 * 画像を詳細表示する
 * 
 * @author chuross
 * 
 */
public class DetailActivity extends Activity {

	private static final String INTENT_KEY_PATH_LIST = "path_list";
	private static final String INTENT_KEY_PAGE = "page";

	public static Intent createIntent(Context context, int position, ArrayList<String> pathList) {
		Intent intent = new Intent(context, DetailActivity.class);
		intent.putExtra(INTENT_KEY_PATH_LIST, pathList);
		intent.putExtra(INTENT_KEY_PAGE, position);

		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);

		@SuppressWarnings("unchecked")
		List<String> pathList = (List<String>) getIntent().getSerializableExtra(INTENT_KEY_PATH_LIST);
		if (pathList == null) {
			return;
		}

		int startPage = getIntent().getIntExtra(INTENT_KEY_PAGE, 0);

		ImagePagerAdapter adapter = new ImagePagerAdapter(DetailActivity.this, pathList);
		ViewPager viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(startPage);
	}
}
