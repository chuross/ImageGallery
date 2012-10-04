package com.asomal.imagegallery.view.image;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.asomal.imagegallery.R;
import com.asomal.imagegallery.domain.image.GetThumbnailImageCommand;
import com.asomal.imagegallery.infrastructure.Command;
import com.asomal.imagegallery.infrastructure.CommandExecuter;

/**
 * 画像をグリッド表示するためのアダプター
 * 
 * @author chuross
 * 
 */
public class ImageGridAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<String> filePathList;
	Context context;

	public ImageGridAdapter(Context context, List<String> filePathList) {
		this.context = context;
		this.filePathList = filePathList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return filePathList.size();
	}

	@Override
	public Object getItem(int position) {
		return filePathList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;

		final ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.image_gridview_row, null);
			final ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress);
			final ImageView imageView = (ImageView) view.findViewById(R.id.grid_imageview);

			holder = new ViewHolder();
			holder.progress = progress;
			holder.imageView = imageView;

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
			holder.imageView.setVisibility(View.INVISIBLE);
			holder.progress.setVisibility(View.VISIBLE);
		}

		final String filePath = filePathList.get(position);
		holder.imageView.setTag(filePath);

		// TODO Tagでの判別どうしよう
		CommandExecuter.post(new GetThumbnailImageCommand(context, filePath), new Command.OnFinishListener<Bitmap>() {

			@Override
			public void onFinished(Bitmap result) {
				if (result == null || !filePath.equals(holder.imageView.getTag().toString())) {
					return;
				}
				holder.imageView.setImageBitmap(result);
				holder.imageView.setVisibility(View.VISIBLE);
				holder.progress.setVisibility(View.GONE);
			}
		});

		return view;
	}

	private class ViewHolder {
		ProgressBar progress;
		ImageView imageView;
	}
}
