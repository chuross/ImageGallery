package com.asomal.imagegallery.view.image;

import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 画像をグリッド表示するためのアダプター
 * 
 * @author chuross
 * 
 */
public class ImageGridAdapter extends BaseAdapter {

	List<Bitmap> bitmapList;

	public ImageGridAdapter(List<Bitmap> bitmapList) {
		this.bitmapList = bitmapList;
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
