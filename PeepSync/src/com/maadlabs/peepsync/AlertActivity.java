package com.maadlabs.peepsync;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

// Activity to display no network connection

public class AlertActivity extends Activity {

	
	AlertDialog alertDialog;
	IntentFilter connectedIntent;
	BroadcastReceiver broadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		init();
	}
	
	public void init()  // initialization of variables
	{
		final Context localContext = this;
		
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(	// Dialog box to display network disconnected
				localContext);

		alertDialogBuilder.setTitle("Network Disconnected!");
 
		alertDialogBuilder
			.setMessage("Check your connection")
				.setCancelable(false);
 
		alertDialog = alertDialogBuilder.create();
		
		broadcastReceiver = new BroadcastReceiver(){

			@Override
			public void onReceive(Context context, Intent intent) {
				
				finish();
			}
			
		};
		connectedIntent = new IntentFilter("com.maadlabs.peepsync.INTERNET_CONNECTION_OK");
		localContext.registerReceiver(broadcastReceiver, connectedIntent);
		alertDialog.show();
	}
	

	
}