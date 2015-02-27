package com.maadlabs.peepsync;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


// display notifiations like new message or new sync request
public class NotificationsActivity extends Activity {

	ImageView displayImage;
	Button acceptButton;
	Button ignoreButton;
	Bitmap displayImageBitmap;
	
	Bundle bundle;
	InputStream serverResponse;
	ServerConnect serverConnect;
	
	ArrayList<NameValuePair> syncParams;
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
	
		init();
		setListeners();
		setDisplay();
	}

	public void init()
	{
		bundle = getIntent().getExtras();
		serverConnect = new ServerConnect();
		syncParams = new ArrayList<NameValuePair>();
		sharedPreferences = this.getSharedPreferences("com.maadlabs.peepsync", Context.MODE_PRIVATE);
		
		displayImage = (ImageView) findViewById(R.id.activity_notifications_display_picture);
		acceptButton = (Button) findViewById(R.id.activity_notifications_accept_button);
		ignoreButton = (Button) findViewById(R.id.activity_notifications_ignore_button);
	}
	
	public void syncResponse()	// connect to server to get info about sync request
	{
		ServerConnect serverConnect = new ServerConnect();

		syncParams.add(new BasicNameValuePair("type", "response"));
		syncParams.add(new BasicNameValuePair("myid", sharedPreferences.getString("fbid", "NA")));
		syncParams.add(new BasicNameValuePair("secondpersonid", bundle.getString("data")));
		serverConnect.HTTPConnect("http://www.maadlabs.com/test/connections.php", syncParams);
	}
	
	public void setListeners()
	{

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(v.getId() == acceptButton.getId())
				{
					syncParams.add(new BasicNameValuePair("sync", "yes"));
				}
				else if(v.getId() == ignoreButton.getId())
				{
					syncParams.add(new BasicNameValuePair("sync", "no"));
				}
				
				new Thread()
				{
					public void run()
					{
						syncResponse();
					}
				}.start();
				finish();
			}
			
		};
		acceptButton.setOnClickListener(onClickListener);
		
		ignoreButton.setOnClickListener(onClickListener);
	}
	
	public void setDisplay()
	{
		new AsyncTask<Void, Void, Void>()		// connect to facebook to get image
		{

			@Override
			protected Void doInBackground(Void... params) {
				Log.i("data", bundle.getString("data"));
				serverResponse = serverConnect.HTTPConnectStream("https://graph.facebook.com/"+bundle.getString("data")+"/picture?type=large", null);
				displayImageBitmap = BitmapFactory.decodeStream(serverResponse);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				displayImage.setImageBitmap(displayImageBitmap);
			}
			
		}.execute();
	}
}
