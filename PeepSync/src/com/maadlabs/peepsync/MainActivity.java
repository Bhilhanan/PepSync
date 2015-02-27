package com.maadlabs.peepsync;


import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

//displays user groups based on interests

public class MainActivity extends FragmentActivity {

	ListView groupsList;
	OnClickListener buttonListener;
	Fragment optionsFragment;
	FrameLayout mainLayout;
	ListView optionsList;
	Button joinGroupButton;
	Intent intent;
	ArrayList<String> groups, myGroups;
	Bundle bundle;
	ArrayAdapter<String> groupsAdapter;
	SharedPreferences sharedPreferences;
	
	int REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
		setListeners();
		openNotificationListeners();
		
		// retrieve state information when orientation changes
		if(savedInstanceState != null)
		{
			 myGroups = savedInstanceState.getStringArrayList("groups");
			 groupsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, myGroups);
			 groupsList.setAdapter(groupsAdapter);
			 groupsAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		  super.onSaveInstanceState(savedInstanceState);
		  // Save UI state changes to the savedInstanceState.
		  savedInstanceState.putStringArrayList("groups", myGroups);
		}
	
	
	public void init()		//  initialization of variables
	{
		sharedPreferences = this.getSharedPreferences("com.maadlabs.peepsync", Context.MODE_PRIVATE);
		
		myGroups = new ArrayList<String>();
		optionsList = (ListView) findViewById(R.id.dashBoardOptions);
		mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
		mainLayout.setOnTouchListener(new OnSwipeTouchListener(this)
		{
			public void onSwipeRight() {
				
				optionsList.setVisibility(View.GONE);
		    }
		    public void onSwipeLeft() {
		    	
				optionsList.setVisibility(View.VISIBLE);
		    }
		   
		});
	
		optionsFragment = (Fragment) getFragmentManager().findFragmentById(R.id.dashBoard);
		groupsList = (ListView) findViewById(R.id.activity_main_groups_list);
		joinGroupButton = (Button) findViewById(R.id.activity_main_join_group);
		
	}
	
	public boolean isOnline(Context context) 
	{
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }
	
	public void openNotificationListeners()		// start service that listens for notification when activity is started
	{
		Context context = getApplicationContext();
		
		if(isOnline(context))
		{
			Log.i("service","started");
			context.startService(new Intent(context, ChatService.class));
		}
		else
		{
			Log.i("service","stopped");
			context.stopService(new Intent(context, ChatService.class));
		}
	}
	public void setListeners()
	{
		bundle = getIntent().getExtras();
		if(bundle != null)
		{
			myGroups.add(bundle.getString("interest"));
		}
		else
		{
			myGroups.add(sharedPreferences.getString("interest", "NA"));
		}
		groupsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, myGroups);
		groupsList.setAdapter(groupsAdapter);
		
		joinGroupButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(MainActivity.this, GroupPickerActivity.class);
			
				startActivityForResult(i, REQUEST_CODE);
			}
			
		});
		
		groupsList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				intent = new Intent(MainActivity.this, GroupInfoActivity.class);
				bundle = new Bundle();
				
				bundle.putString("group", parent.getAdapter().getItem(position).toString());
				intent.putExtras(bundle);
				
				startActivity(intent);
				
			}
			
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	  if (resultCode == RESULT_OK) {
	    if (!(data.getExtras() == null)) 
	    {
	    	Bundle tmpBundle = data.getExtras();
	    	int i = 0;
	    	String[] newGroups = tmpBundle.getStringArray("newGroups");
	    	
	    	while(i < newGroups.length)
	    	{
	    	//	if(!newGroups[i].equals(bundle.getString("interest")))
	    			myGroups.add(newGroups[i]);
	    		Log.i("tmp", newGroups[i]);
	    		i++;
	    	}
	    	
	    	groupsAdapter.notifyDataSetChanged();
	    }
	  }
	} 
}
