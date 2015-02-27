package com.maadlabs.peepsync;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// display information about each person
public class PersonInfoActivity extends Activity {

	Bundle bundle, sendBundle;
	Button chatButton, syncButton;
	TextView nameText, aboutText, interestText;
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		init();
		setListeners();
		
	}
	
	public void init()
	{
		sharedPreferences = this.getSharedPreferences("com.maadlabs.peepsync", Context.MODE_PRIVATE);
		getData();
		displayInfo();
	}
	public void getData()
	{
		bundle = getIntent().getExtras();
	}
	
	public void displayInfo()
	{
		nameText = (TextView) findViewById(R.id.person_info_name);
		aboutText = (TextView) findViewById(R.id.person_info_about);
		interestText = (TextView) findViewById(R.id.person_info_interest);
		chatButton = (Button) findViewById(R.id.person_info_chat_button);
		syncButton = (Button) findViewById(R.id.person_info_sync_button);
		syncButton.setVisibility(View.GONE);
		
		nameText.setText(bundle.getString("name"));
		aboutText.setText(bundle.getString("interest"));
		interestText.setText(bundle.getString("about"));
		
		syncStatus(sharedPreferences.getString("fbid", null), bundle.getString("id"));
	}
	
	public void setListeners()
	{
		chatButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(PersonInfoActivity.this, ChatActivity.class);
				sendBundle = new Bundle();
				sendBundle.putString("from", sharedPreferences.getString("fbid", null));
				sendBundle.putString("to", bundle.getString("id"));
				intent.putExtras(sendBundle);
				startActivity(intent);	
			}
			
		});
		
		syncButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {

				new AsyncTask<Void, Void, Void>() {

					String output = null;
					@Override
					protected Void doInBackground(Void... args) {

						ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("myid", sharedPreferences.getString("fbid", null) ));
						params.add(new BasicNameValuePair("secondpersonid", bundle.getString("id")));
						params.add(new BasicNameValuePair("type", "request"));
						output = (new ServerConnect()).HTTPConnect("http://www.maadlabs.com/test/connections.php", params);
						Log.i("response2", output);
						return null;
					}
					
					protected void onPostExecute(Void result) {
						
						if(output.contains("CONNECTION_REQUEST_ADDED"))
						{
							Toast.makeText(getApplicationContext(), "Sync Request Sent", Toast.LENGTH_LONG).show();
							syncButton.setVisibility(View.GONE);
						}
					}
				}.execute();
				
			}
			
		});
	}
	
	public void syncStatus(final String self, final String secondperson)
	{
		new AsyncTask<Void, Void, Void>() {

			String output = null;
			@Override
			protected Void doInBackground(Void... args)
			{
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("myid", self));
				params.add(new BasicNameValuePair("secondpersonid", secondperson));
				params.add(new BasicNameValuePair("type", "check"));
				output = (new ServerConnect()).HTTPConnect("http://www.maadlabs.com/test/connections.php", params);
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				Log.i("output", output);
				if(output.contains("CONNECTION_NOT_FOUND"))
				{
					syncButton.setVisibility(View.VISIBLE);
				}
			}
			
		}.execute();
	}
}