package com.maadlabs.peepsync;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {

	private static GoogleMap world;
	private LocationManager locMan;
	private Marker pin;
	public static LatLng lastLatLng;
	private Intent intent;
	
	//For testing purpose only.
	String Batman="I'm the reason the criminals breath easy when the sun comes up.";
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		intent=new Intent(this,MapMarkerInfo.class);
		if(world==null){
			/*
			 * need this if condition so that every time the orientation of the screen
			 * is changed and onCreate() is called we don't create a new object. We have
			 * to create a new object only once.
			 */
			world=((MapFragment)getFragmentManager().findFragmentById(R.id.themap)).getMap();
		}
		if(world!=null){
//			world.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			world.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			world.getUiSettings().setScrollGesturesEnabled(false);
			world.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(Marker m) {
					if(m.getTitle().equals("You")){
						
					}
					else{
						intent.putExtra("id", m.getTitle());
						startActivity(intent);
					}
					return false;
				}
				
			});
			updatePlaces();
			//this is for sample user list. Delete this array list once you have actual data
			
			populateWorld(TempUserList.getUserList(this));
		}
			
		//populateWorld(TempUserList.getUserList(this));
	}
	
	public void updatePlaces(){
		locMan=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location lastLoc=locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		lastLatLng=new LatLng(lastLoc.getLatitude(),lastLoc.getLongitude());
		if(pin!=null){
			/*
			 * When user's location changes, the previous marker is removed and
			 * replaced with new one.
			 */
			pin.remove();
			
		}
		world.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,14.0f),3000,null);
		//centerOnMe();
		world.addMarker(new MarkerOptions()
		.position(lastLatLng)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
		.title("You")
		//.snippet(Batman)
		.alpha(0.4f));
		//draw a circle around the user marking the range
		Circle 	circle=world.addCircle(new CircleOptions()
				.center(lastLatLng)
				.radius(1600) //1.6 km
				.strokeColor(0xff0099cc)
				.strokeWidth(5)
				.fillColor(0x400099cc)); //Holo blue dark

	}


	private void populateWorld(ArrayList<User> list){
		LatLng loc=lastLatLng;
		double lat=loc.latitude;;
		double lon=loc.longitude;
		
		for(User e:list){
			world.addMarker(new MarkerOptions()
			.position(new LatLng(e.getLat(),e.getLon()))
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
			//.snippet(e.getInfo())
			.title(e.getId()));
		}
		
			
		
	}

	public static void centerOnMe(){
		world.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,14.0f),3000,null);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_map, menu);
		return true;
	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_list:
			Intent intent=new Intent(this, MapListViewActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_exit:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	
}