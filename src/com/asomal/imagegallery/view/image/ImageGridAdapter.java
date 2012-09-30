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

/**
 * 画像をグリッド表示するためのアダプター
 * 
 * @author chuross
 * 
 */
public class ImageGridAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<Bitmap> bitmapList;

	public ImageGridAdapter(Context context, List<Bitmap> bitmapList) {
		this.bitmapList = bitmapList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return bitmapList.size();
	}

	@Override
	public Object getItem(int position) {
		return bitmapList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			view = inflater.inflate(R.layout.image_gridview_row, null);
		}

		ProgressBar progress = (ProgressBar) view.findViewById(R.id.progress);
		ImageView imageView = (ImageView) view.findViewById(R.id.grid_imageview);

		return null;
	}
}
