package com.maadlabs.peepsync;

import com.google.android.maps.MapActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MapListViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maplist);
		
		ListView lv=(ListView)findViewById(R.id.listview);
		ListAdapter adapter=new ListAdapter(this);
		lv.setAdapter(adapter);
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_scanner:
			Intent intent=new Intent(this, MapActivity.class);
			startActivity(intent);
			finish();
			return true;
		case R.id.action_exit:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}