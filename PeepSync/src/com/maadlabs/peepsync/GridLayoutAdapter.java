package com.maadlabs.peepsync;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class GridLayoutAdapter extends BaseAdapter {
	private Context context;
	private final Bitmap[] images;
	int resource;
	Holder holder;
	View row;
	
	public GridLayoutAdapter(Context context, int resource, Bitmap[] images) {
		this.context = context;
		this.images = images;
		this.resource = resource;
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
 
		row = convertView;
		
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
 
		if (convertView == null) {
		
			row = inflater.inflate(resource, parent, false);
			holder = new Holder();
 
			// set image based on selected text
			holder.image = (ImageView) row
					.findViewById(R.id.group_info_grid_item);
		row.setTag(holder);
		

		} else {
			row = convertView;
			holder = (Holder) row.getTag();
		}
 
		holder.image.setImageBitmap(images[position]);
		return row;
	}
 
	@Override
	public int getCount() {
		return images.length;
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	static class Holder
	{
		ImageView image;
	}
 
}
