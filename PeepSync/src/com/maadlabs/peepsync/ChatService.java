package com.maadlabs.peepsync;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

// Checks for new notifications like new messages or new sync requests

public class ChatService extends Service{

	SharedPreferences sharedPreferences;
	ConnectivityManager connectivityManager;
	String fbid;
	ServerConnect serverConnect;
	ArrayList<NameValuePair> params;
	NetworkInfo networkActive;
	AsyncTask<String, Integer, String> asyncTask;

	String serverResponse;
	IntentFilter newMessageIntentFilter;
	BroadcastReceiver broadcastReceiver;
	String newMessage = null;
	Thread serviceThread;
	
	
	public void onMyCreate()
	{

		Log.i("on","serviceCreate");
	
		serverConnect = new ServerConnect();
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		sharedPreferences = getSharedPreferences("com.maadlabs.peepsync", Context.MODE_PRIVATE);
		fbid = new String();
		fbid = sharedPreferences.getString("fbid", null);
		
		params = new ArrayList<NameValuePair>();
	}
	
	 @Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		 
		 onMyCreate();
		 params.add(new BasicNameValuePair("flag", 0+""));
		 params.add(new BasicNameValuePair("to", fbid));
		 params.add(new BasicNameValuePair("type", "newrequest"));

		 newMessageIntentFilter = new IntentFilter("com.maadlabs.peepsync.RECEIVED_MESSAGE_TEXT");
		 newMessageIntentFilter.setPriority(1000);
		 getApplicationContext().registerReceiver(broadcastReceiver, newMessageIntentFilter);
		 
		 broadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				createNotification(getApplicationContext(), newMessage);
			}
			 
		 };
  
			 serviceThread = new Thread()		// Thread that runs the actual service of checking notifications
			 {
				 public void run()
				 {
					 while(Thread.interrupted() != true)
					 {
						 serverResponse = serverConnect.HTTPConnect("http://www.maadlabs.com/test/service.php", params); 
						 
						 if(serverResponse.contains("connection") || serverResponse.contains("message"))
						 {
							 
							 Log.i("resp", serverResponse);
							 JSONObject jObject;
							 JSONObject jObject2 = null;
							try {
								jObject = new JSONObject(serverResponse);
								jObject2 = jObject.getJSONObject("notification");
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							 
							 
							 if(serverResponse.contains("messages"))		// if new message found
							 {
									 
								 Intent i = new Intent("com.maadlabs.peepsync.RECEIVED_MESSAGE_TEXT");
								 Bundle extras = new Bundle();
								 try 
								 { 
									 newMessage = jObject2.toString();
								 }
								 catch(Exception e)
								 {
									 e.printStackTrace();
								 }
		
								 extras.putString("data", newMessage);
								 i.putExtras(extras);
								 getApplicationContext().sendOrderedBroadcast(i, null);
							 }
							 
							 if(serverResponse.contains("connection"))	// if new connection found
							 {
								 ArrayList<String> requests = new ArrayList<String>();
								try {
									requests = parseJSONforSyncRequest(jObject2.getJSONArray("connection"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								 createNotificationForSyncRequest(getApplicationContext(), requests.get(0));
							 }

						 }
						 else
						 {
							 try {
								Thread.sleep(4000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						 }
					 }
				 }
			 };
			 
			 serviceThread.start();
			 
		 return Service.START_STICKY;
	 }
	 
	 @Override
	 public void onDestroy()
	 {
		 serviceThread.interrupt();
	 }
	 
	public ArrayList<String> parseJSONforSyncRequest(JSONArray data) throws Exception
	{
		ArrayList<String> requests = new ArrayList<String>();
		
		
		JSONArray jRequestArray = data;
		
		String newRequest;
		
		for(int i = 0; i < jRequestArray.length(); i++)
		{
			newRequest = (String) jRequestArray.get(i);
			
			requests.add(newRequest);
		}
		return requests;
	}
	 
	 public Chat parseReceivedText(String data) throws Exception
		{
			ArrayList<Chat> messages = new ArrayList<Chat>();
			
			Log.i("data",data);
			JSONObject jObject = new JSONObject(data);
			
			JSONArray jMessageArray = jObject.getJSONArray("messages");
			
			Chat newMessage = null;
			
			for(int i = 0; i < jMessageArray.length(); i++)
			{
				newMessage = new Chat();
				JSONObject currObject = jMessageArray.getJSONObject(i);
				
				newMessage.fromId = (String) currObject.get("from");
				newMessage.textMessage = (String) currObject.getString("message");
				messages.add(newMessage);
			}
			return newMessage;
		}
	 
	 @SuppressLint("NewApi")
	public void createNotificationForSyncRequest(Context context, String data) {
	    	
		    Intent myIntent = new Intent(context,  NotificationsActivity.class);
		    Bundle extras = new Bundle();
		    extras.putString("data", data);
		    myIntent.putExtras(extras);
		    
		    PendingIntent pIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
		   
			    
		   
		    Notification new_notification = new Notification.Builder(this)
		        .setContentTitle("New Sync Request")
		        .setContentText(data+"")
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pIntent)  // the activity to start after the notification is clicked
		        .build();
		    
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    new_notification.flags |= Notification.FLAG_AUTO_CANCEL;  // cancel notification after it is selected
		    
		    notificationManager.notify(0, new_notification);

		  }
	
	 @SuppressLint("NewApi")
	public void createNotification(Context context, String data) {
	    	
		    Intent intent = new Intent(context, ChatActivity.class);
		    Bundle extras = new Bundle();
		    extras.putString("data", data);
		    intent.putExtras(extras);
		    
		    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		    Chat newMessage = new Chat();
			try {
				newMessage = parseReceivedText(data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    Notification new_notification = new Notification.Builder(this)
		        .setContentTitle("New Message")
		        .setContentText(newMessage.textMessage+"")
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pIntent)  // the activity to start after the notification is clicked
		        .build();
		    
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    new_notification.flags |= Notification.FLAG_AUTO_CANCEL;  // cancel notification after it is selected
		    
		    notificationManager.notify(0, new_notification);

		  }
	 
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
