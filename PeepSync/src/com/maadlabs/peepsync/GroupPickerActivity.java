package com.maadlabs.peepsync;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;


// displays list of groups available
public class GroupPickerActivity extends Activity {

	Bundle bundle;
	ArrayList<CheckBoxItem> groups;
	ListView groupsList;
	CustomListAdapter<CheckBoxItem> groupsAdapter;
	CheckBoxHolder holder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_picker);

		init();		// variables initialization
	}
	
	public void init()
	{
		groupsList = (ListView) findViewById(R.id.activity_group_picker_groups_list);
		addGroups();
		
		groupsAdapter = new CustomListAdapter<CheckBoxItem>(this, R.layout.listitem_layout_checklist, groups){
			
			public void holderInitIf()
			{
				holder = new CheckBoxHolder();
				holder.item = (CheckBox) row.findViewById(R.id.listitem_layout_checkbox);
				row.setTag(holder);
			}
			
			public void holderInitElse()
			{
				holder = (CheckBoxHolder) row.getTag();
			}
			
			public void setValues()
			{
				holder.item.setText(options.get(myPosition).groupId);
			}
		};
		
		groupsList.setAdapter(groupsAdapter);
	}
	
	public void addGroups()		// adds these groups statically
	{
		groups = new ArrayList<CheckBoxItem>();
		groups.add(new CheckBoxItem("Android"));
		groups.add(new CheckBoxItem("Soccer"));
		groups.add(new CheckBoxItem("Reading group"));
		groups.add(new CheckBoxItem("Pokemon"));
		groups.add(new CheckBoxItem("Football"));
		groups.add(new CheckBoxItem("Travel Buddies"));
	}
	
	@Override
	public void finish()	// pass the chosen groups back to the calling activity
	{
		Log.i("Tag", "finish");
		Bundle bundle = new Bundle();
		Intent intent = new Intent();
		int index = 0, i = 0, checkedCnt = 0;
		
		Log.i("cnt", groupsList.getCheckedItemCount()+"");
	
		
		while(index < groupsList.getChildCount())
		{
			LinearLayout tmpLayout = (LinearLayout) groupsList.getChildAt(index);
			CheckBox tmpBox = (CheckBox) tmpLayout.getChildAt(0);
			if(tmpBox.isChecked())
			{
				checkedCnt++;
			}
			index++;
		}

		index = 0;
		i = 0;
		if(checkedCnt > 0)
		{
			String[] newGroups = new String[checkedCnt];
			
			while(index < groupsList.getCount())
			{
				LinearLayout tmpLayout = (LinearLayout) groupsList.getChildAt(index);
				CheckBox tmpBox = (CheckBox) tmpLayout.getChildAt(0);
				
				if(tmpBox.isChecked())
				{
					newGroups[i] = (String) tmpBox.getText();
					Log.i("item", newGroups[i]);
					i++;
				}
				index++;
			}
			
			bundle.putStringArray("newGroups", newGroups);
			intent.putExtras(bundle);
		}

		setResult(RESULT_OK, intent);
		super.finish();
	}
	

}
