package com.maadlabs.peepsync;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DashBoardFragment extends Fragment {

	ListView dashBoardList;
	View v;
	CustomListAdapter<DashBoard> dashBoardAdapter;
	ArrayList<DashBoard> dashBoardOptions;
	ListView optionsList;
	OnItemClickListener listItemListener;
	OptionHolder holder;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance)
	{
		dashBoardOptions = new ArrayList<DashBoard>();
		String options[] = { "Recent", "Connections", "Search", "Map", "Settings" };
		for(int i = 0; i < options.length; i++)
		{
			dashBoardOptions.add(new DashBoard(options[i], null));
		}
		
		v = inflater.inflate(R.layout.fragment_dash_board, parent, false);
		dashBoardAdapter = new CustomListAdapter<DashBoard>(v.getContext(), R.layout.board_option_layout, dashBoardOptions)
		{
			public void holderInitIf()
			{
				holder = new OptionHolder();
				holder.option_holder  = (TextView) row.findViewById(R.id.dash_board_option);
				holder.image_holder = (ImageView) row.findViewById(R.id.das_board_option_image);
				row.setTag(holder);
			}
			
			public void holderInitElse()
			{
				holder = (OptionHolder) row.getTag();
			}
			
			public void setValues()
			{
				DashBoard option = (DashBoard) options.get(myPosition);
				holder.option_holder.setText(option.option);
			}
		};
		dashBoardList = (ListView) v.findViewById(R.id.dashBoardOptions);
		dashBoardList.setPadding(20, 20, 20, 20);
		dashBoardList.setAdapter(dashBoardAdapter);
		return v;
	}
	
	public void onStart()
	{
		super.onStart();
		optionsList = (ListView) v.findViewById(R.id.dashBoardOptions);
		listItemListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				DashBoard item = (DashBoard) parent.getAdapter().getItem(position);
				
				Intent activity = null;
				
				if((item.option).equals("Recent"))
				{
					activity = new Intent(v.getContext(), RecentActivity.class);
				
				}
				else if((item.option).equals("Connections"))
				{
					activity = new Intent(v.getContext(), ConnectionsActivity.class);
					
				}
				else if((item.option).equals("Search"))
				{
					activity = new Intent(v.getContext(), SearchActivity.class);
					
				}
				else if((item.option).equals("Settings"))
				{
					activity = new Intent(v.getContext(), SettingsActivity.class);
					
				}
				else if((item.option).equals("Map"))
				{
					activity = new Intent(v.getContext(), MapActivity.class);
					
				}
				
					v.getContext().startActivity(activity);
				
				}
			
			
		};
		optionsList.setOnItemClickListener(listItemListener);
		
	}
	


}
