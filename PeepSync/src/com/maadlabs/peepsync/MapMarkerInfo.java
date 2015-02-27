package com.maadlabs.peepsync;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MapMarkerInfo extends Activity {

	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mapmarkerinfo);

		id=getIntent().getStringExtra("id");
		// Get information for this id ///////////////////////////
		User user=null;
		ArrayList<User> userList=TempUserList.getUserList(this);
		for(User e:userList){
			if(e.getId().equals(id)){
				user=e;
				break;
			}
		}
		
		//////////////////////////////////////////////////////////
		RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.layout.layout_mapmarkerinfo);
		//relativeLayout.setBackground(new ColorDrawable(Color.TRANSPARENT));
		TextView tv_title=(TextView)findViewById(R.id.layout_markerInfo_title);
		TextView tv_info=(TextView)findViewById(R.id.layout_markerInfo_descp);
		tv_title.setText(user.getId());
		tv_info.setText(user.getInfo());
		
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return super.onOptionsItemSelected(item);
	}

	/*
	@Override
	public void finish() {
		MapActivity.centerOnMe();
		super.finish();
	}

	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}
*/
	
	
}