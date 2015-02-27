package com.maadlabs.peepsync;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


// search user based on info

public class SearchActivity extends Activity {

	EditText searchBox;
	Button searchButton;
	String searchQuery, serverResponse;
	ServerConnect serverConnect;
	ArrayList<ResultInfo> searchResults;
	CustomListAdapter<ResultInfo> listAdapter;
	ListView resultsListView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		init();		// variables initialization
		setListeners();	// adapter listeners setting
	}

	public void init()
	{
		searchBox = (EditText) findViewById(R.id.activity_search_query);
		searchButton = (Button) findViewById(R.id.activity_search_button);
		resultsListView = (ListView) findViewById(R.id.activity_search_resultslist);
		searchResults = new ArrayList<ResultInfo>();
		
		adapterInit();
	}
	
	public void setListeners()
	{
		searchButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {

				searchQuery = searchBox.getText().toString();
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... values) {

						serverConnect = new ServerConnect();
						
						ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("query", searchQuery));
						
						serverResponse = serverConnect.HTTPConnect("http://www.maadlabs.com/test/search.php", params);
						return null;
					}
					@Override
					protected void onPostExecute(Void result)
					{
						try {
							Log.i("serverResponse", serverResponse);
							searchResults.clear();
							parseJSON(serverResponse);
							listAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}.execute();
				
			}
			
		});
		
		resultsListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i;
				Bundle bundle = new Bundle();
				
				ResultInfo item = (ResultInfo) parent.getAdapter().getItem(position);
				
				if(item.type == 1)
				{
					bundle.putString("id", item.id);
					bundle.putString("name", item.name);
					bundle.putString("interest", item.groups);
					bundle.putString("about", item.about);
					
					i = new Intent(SearchActivity.this, PersonInfoActivity.class);
					i.putExtras(bundle);
				}
				else
				{
					bundle.putString("name", item.groupName);
					
					i = new Intent(SearchActivity.this, GroupInfoActivity.class);
					i.putExtras(bundle);
				}
				startActivity(i);
			}
			
		});
	}
	
	public void parseJSON(String arg) throws Exception
	{
		ResultInfo resultInfo;

		JSONObject jObject = new JSONObject(arg);
		JSONObject jUsers = jObject.getJSONObject("results");
		
		if(jUsers.has("group"))
		{
			JSONArray jGroupArray = jUsers.getJSONArray("group");
			for(int i = 0; i < jGroupArray.length(); i++)
			{
				resultInfo = new ResultInfo(2);
				JSONObject currObject = jGroupArray.getJSONObject(i);
				
				resultInfo.groupName = (String) currObject.get("name");
			
				searchResults.add(resultInfo);
			}
		}
		
		if(jUsers.has("user"))
		{
			JSONArray jUserArray = jUsers.getJSONArray("user");
			
			for(int i = 0; i < jUserArray.length(); i++)
			{
				resultInfo = new ResultInfo(1);
				JSONObject currObject = jUserArray.getJSONObject(i);
				
				resultInfo.name = (String) currObject.get("name");
				resultInfo.groups = (String) currObject.get("interest");
				resultInfo.about = (String) currObject.get("about");
				resultInfo.id = (String) currObject.get("id");
				searchResults.add(resultInfo);
			}
		}
		
	}
	
	public void adapterInit()
	{
		listAdapter = new CustomListAdapter<ResultInfo>(getApplicationContext(), R.layout.listitem_layout_results, searchResults)
		{	
			ResultsHolder holder;
			
			public void holderInitIf()
			{
				holder = new ResultsHolder();
				holder.name  = (TextView) row.findViewById(R.id.search_name);
				holder.about = (TextView) row.findViewById(R.id.search_about);
				holder.groups = (TextView) row.findViewById(R.id.search_groups);
			
				holder.displayImage = (ImageView) row.findViewById(R.id.search_display_picture);
				
				row.setTag(holder);
			}
			
			public void holderInitElse()
			{
				holder = (ResultsHolder) row.getTag();
			}
			
			public void setValues()
			{
				ResultInfo option = options.get(myPosition);
				if(option.type == 1)
				{
					holder.name.setText(option.name);
					holder.about.setText(option.about);
					holder.groups.setText(option.groups);
				}
				else
				{
					holder.name.setText(option.name);
					holder.name.setBackgroundColor(422);
				}
			}
		};
		resultsListView.setAdapter(listAdapter);
	}
	
}
