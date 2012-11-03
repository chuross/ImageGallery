package com.asomal.imagegallery.view.image;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asomal.imagegallery.R;
import com.asomal.imagegallery.domain.image.GetDetailImageCommand;
import com.asomal.imagegallery.domain.image.ImageCache;
import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.infrastructure.CommandExecuter;

public class ImagePagerAdapter extends PagerAdapter {

	private Context context;
	private List<String> pathList;
	private ImageCache cache;

	/**
	 * @param context {@link Context}
	 * @param pathList ファイルパスリスト
	 */
	public ImagePagerAdapter(Context context, List<String> pathList) {
		this.context = context;
		this.pathList = pathList;
		cache = new ImageCache(context, 1, ImageCache.Type.DETAIL);
	}

	@Override
	public int getCount() {
		return pathList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final String path = pathList.get(position);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.image_pager_row, container, false);

		final ImageView imageView = (ImageView) view.findViewById(R.id.image_pager_row_bitmap);
		imageView.setVisibility(View.INVISIBLE);
		imageView.setTag(path);

		CommandExecuter.post(new GetDetailImageCommand(context, path, cache), new Command.OnFinishListener<Bitmap>() {

			@Override
			public void onFinished(Bitmap result) {
				if (result == null || !path.equals(imageView.getTag().toString())) {
					return;
				}

				imageView.setImageBitmap(result);
				imageView.setVisibility(View.VISIBLE);
			}
		});

		container.addView(view);

		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object view) {
		container.removeView((View) view);
	}

}
