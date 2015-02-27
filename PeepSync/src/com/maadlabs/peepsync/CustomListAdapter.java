package com.maadlabs.peepsync;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter<T> extends ArrayAdapter<T> {

	Context context;
	int resource, myPosition;
	ArrayList<T> options;
	View row;
	
	public CustomListAdapter(Context context, int resource, ArrayList<T> options) {
		super(context, resource, options);
		this.context = context;
		this.resource = resource;
		this.options = options;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(resource, parent, false);  // like a template to add values
			
			holderInitIf();
		}

		else
		{
			row = convertView;
			holderInitElse();
		}
		
		myPosition = position;
		setValues();
	
		return row;
	}

	public void holderInitIf()
	{
		
	}
	
	public void holderInitElse()
	{
		
	}
	
	public void setValues()
	{
		
	}

	
}
