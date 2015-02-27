package com.maadlabs.peepsync;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	private ArrayList<User> userList=new ArrayList<User>();
	LayoutInflater inflater;
	Bitmap image;
	public ListAdapter(Context context){
		image=BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_user);
		userList=TempUserList.getUserList(context);
	}
	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		if(view==null){
			Log.e("Info","Inflating view");
			inflater=LayoutInflater.from(parent.getContext());
			view=inflater.inflate(R.layout.data_model, parent,false);
		}
		User user=userList.get(position);
		TextView tv=(TextView)view.findViewById(R.id.textline);
		ImageView image=(ImageView)view.findViewById(R.id.datamodel_image);
		TextView tv_lat=(TextView)view.findViewById(R.id.lat);
		TextView tv_lon=(TextView)view.findViewById(R.id.lon);
		tv.setText(user.getId());
		image.setImageBitmap(user.getDisplayPic());
		tv_lat.setText("Lat: "+user.getLat());
		tv_lon.setText("Lon: "+user.getLon());
		
		return view;
	}

}
