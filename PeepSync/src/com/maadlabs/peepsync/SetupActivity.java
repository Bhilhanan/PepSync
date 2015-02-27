package com.maadlabs.peepsync;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// gets user interests
public class SetupActivity extends Activity {

	ImageView displayPicture;
	TextView displayName;
	Bundle bundle;
	Bitmap picture;
	Button completeButton;
	Intent mainIntent;
	AutoCompleteTextView displayInterests;
	ArrayList<String> interests;
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
	
		init();	// variables initialization
		makeSetupOptions();		// show info and get interest
		addListeners();		// adding listeners for views
	}
	
	public void addInterests()
	{
		interests = new ArrayList<String>();
		interests.add("Android");
		interests.add("Soccer");
		interests.add("Reading group");
		interests.add("Pokemon");
		interests.add("Football");
		interests.add("Travel Buddies");
	}
	public void init()
	{
	
		sharedPreferences = this.getSharedPreferences("com.maadlabs.peepsync", Context.MODE_PRIVATE);
		bundle = getIntent().getExtras();
		
		displayName = (TextView) findViewById(R.id.activity_setup_display_name);
		displayName.setText(bundle.getString("display_name"));
		
		displayPicture = (ImageView) findViewById(R.id.activity_setup_display_picture);
		
		displayInterests = (AutoCompleteTextView) findViewById(R.id.activity_setup_interests_list);
		completeButton = (Button) findViewById(R.id.activity_setup_complete);
		displayPicture.setImageBitmap((Bitmap) bundle.getParcelable("display_picture"));
	}
	
	public void makeSetupOptions()
	{
		addInterests();
		displayInterests.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,interests));
	}
	
	public void addListeners()
	{
		completeButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				callActivity();
			}
			
		});
	}
	
	public void callActivity()
	{
		mainIntent = new Intent(SetupActivity.this, MainActivity.class);
		bundle = new Bundle();
		sharedPreferences.edit().putString("interest", displayInterests.getText().toString()).commit();
		bundle.putString("interest", displayInterests.getText().toString());
		
		new AsyncTask<Void, Void, Void>()
		{

			@Override
			protected Void doInBackground(Void... params) {
				
				ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
				values.add(new BasicNameValuePair("interest", displayInterests.getText().toString()));
				values.add(new BasicNameValuePair("fbid", sharedPreferences.getString("fbid", "NA")));
				
				(new ServerConnect()).HTTPConnect("http://www.maadlabs.com/test/interest.php", values);
				return null;
			}
			
		}.execute();
		mainIntent.putExtras(bundle);
		startActivity(mainIntent);
	}
	
}
