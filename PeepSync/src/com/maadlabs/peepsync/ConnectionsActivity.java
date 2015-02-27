package com.maadlabs.peepsync;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

// display a users connections

@SuppressLint("HandlerLeak")
public class ConnectionsActivity extends FragmentActivity {

	Button optionsButton;
	OnClickListener buttonListener;
	CustomListAdapter<Connection> listAdapter;
	ListView peopleList, optionsList;
	ArrayList<Connection> people;
	ConnectionHolder holder;
	String file;
	Handler handler;
	ServerConnect serverConnect;
	AsyncTask<String, Integer, String> serverTask;
	SharedPreferences sharedPreferences;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connections);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		init();		// variable initialization
		
		optionsList = (ListView) findViewById(R.id.dashBoardOptions);
		
		peopleList.setOnTouchListener(new OnSwipeTouchListener(this)
		{
			public void onSwipeRight() {
				optionsList.setVisibility(View.GONE);
		    }
		    public void onSwipeLeft() {
				optionsList.setVisibility(View.VISIBLE);
		    }
		   
		});
	}
	
	public void init()
	{
		sharedPreferences = this.getSharedPreferences("com.maadlabs.peepsync", Context.MODE_PRIVATE);
		
		peopleList = (ListView) findViewById(R.id.connections_activity_peopleList);
		people = new ArrayList<Connection>();
		serverConnect = new ServerConnect();
		getServerData();
		peopleList.setOnItemClickListener(new OnItemClickListener()
		{

			Connection item = new Connection();
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getApplicationContext(), PersonInfoActivity.class);
				item = (Connection) parent.getAdapter().getItem(position);
				
				i.putExtra("id", item.id);
				i.putExtra("name", item.name);
				i.putExtra("interest", item.interest);
				i.putExtra("about", item.about);
				startActivity(i);
			}
			
		});
		
	}
	
	public void getServerData()
	{
		serverTask = new AsyncTask<String, Integer, String>()
			{

			@Override
			protected String doInBackground(String... args) {
				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("myid", sharedPreferences.getString("fbid", "NA")));
				file = serverConnect.HTTPConnect("http://www.maadlabs.com/test/peeps.php", params);
				
				return null;
			}
			
			protected void onPostExecute(String result)
			{
				try {
					parseJSONResults(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				adapterInit();
			}
		};
		
		serverTask.execute(new String[0]);

	}
	
	public void parseJSONResults(String arg) throws Exception
	{
		Log.i("op", arg);
		JSONObject jObject = new JSONObject(arg);
		JSONObject jUsers = jObject.getJSONObject("people");
		
		JSONArray jUserArray = jUsers.getJSONArray("user");
		
		for(int i = 0; i < jUserArray.length(); i++)
		{
			JSONObject currObject = jUserArray.getJSONObject(i);
			Connection connection = new Connection();
			
			connection.id = (String) currObject.getString("id");
			connection.name = (String) currObject.get("name");
			connection.interest = (String) currObject.get("interest");
			connection.about = (String) currObject.get("about");
			Log.i("size", people.size()+"");
			people.add(connection);
		}
	}
	
	
	public void adapterInit()
	{
		listAdapter = new CustomListAdapter<Connection>(getApplicationContext(), R.layout.listitem_layout_connections, people)
				{
					public void holderInitIf()
					{
						holder = new ConnectionHolder();
						holder.name_holder  = (TextView) row.findViewById(R.id.connections_username);
						holder.interests_holder = (TextView) row.findViewById(R.id.connections_interest);
						holder.about_holder = (TextView) row.findViewById(R.id.connetions_about);
						row.setTag(holder);
					}
					
					public void holderInitElse()
					{
						holder = (ConnectionHolder) row.getTag();
					}
					
					public void setValues()
					{
						Connection option = options.get(myPosition);
						holder.name_holder.setText(option.name);
						holder.about_holder.setText(option.about);
						holder.interests_holder.setText(option.interest);
					}
				};
				peopleList.setAdapter(listAdapter);
	}
	
	
}

