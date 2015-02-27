package com.maadlabs.peepsync;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

// Instantiates new service for listening for new messages based on network state
public class ChatBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

		if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE"))
		{
			if(isOnline(context))	// start listening for new messages if online
			{
				Log.i("service","started");
				context.sendBroadcast(new Intent("com.maadlabs.peepsync.INTERNET_CONNECTION_OK"));
				context.startService(new Intent(context, ChatService.class));
			}
			else	
			{		// stop service and show "network not found" message.
				
				Log.i("service","stopped");
				context.stopService(new Intent(context, ChatService.class));
				
				Intent i = new Intent(context, AlertActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
		}
	}
	
	
	 public boolean isOnline(Context context) {
         ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo netInfo = cm.getActiveNetworkInfo();

         if (netInfo != null && netInfo.isConnected()) {
             return true;
         }
         return false;
     }

}
