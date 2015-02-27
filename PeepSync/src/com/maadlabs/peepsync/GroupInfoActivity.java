package com.maadlabs.peepsync;



import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

//shows information about each group
// displays group members and group description
public class GroupInfoActivity extends Activity {

	TextView groupDescription;
	GridView groupMembers;
	GridLayoutAdapter adapter;
	Bitmap images[];
	ArrayList<Bitmap> tmpImages;
	Bundle bundle;
	Group group;
	Runnable secondRunnable;
	Bitmap profilePicture;
	URL picURL;
	String imgSource;
	String file;
	AsyncTask<String, Integer, String> asyncTask;
	InputStream serverResponse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info);
			
		init();		// variable initialization
		getGroupInfo();
	}
	
	public void init()
	{
		imgSource = new String();
		group = new Group();
		tmpImages = new ArrayList<Bitmap>();
		bundle = getIntent().getExtras();
		groupDescription = (TextView) findViewById(R.id.activity_group_info_description);
		groupMembers = (GridView) findViewById(R.id.activity_group_info_people);
		
		setListeners();		// set listeners for group members to display information when clicked
	}
	
	public void setListeners()
	{
		groupMembers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {


				
			}
			
		});
	}
	public void getGroupInfo()
	{
		getDataFromServer();	// gets data from server when clicked
	}
	
	public ArrayList<String> makeList(String[] array)
	{
		int i = 0;
		ArrayList<String> tmpList = new ArrayList<String>();
		while(i < array.length)
		{
			tmpList.add(array[i]);
			i++;
		}
		
		return tmpList;
		
	}
	
	@SuppressLint("HandlerLeak")
	public void parseJSON() throws Exception
	{
		JSONObject jObject = new JSONObject(file);
		JSONObject jGroup = jObject.getJSONObject("group");
		group.info = (String) jGroup.get("info");
		String[] tmpMembers = ((String) jGroup.get("members")).split(",");
		
		group.members = makeList(tmpMembers);
		
	}
	
	public void populateGroupDisplay()
	{
			getImages();
	}
	public void copyListToArray()
	{
		int i = 0;
		images = new Bitmap[tmpImages.size()];
		while(i < tmpImages.size())
		{
			Log.i("ivalue",i+"");
			images[i] = tmpImages.get(i);
			i++;
		}
	}
	public void getDataFromServer()
	{

		AsyncTask<String, Integer, String> groupsTask;
		
		groupsTask = new AsyncTask<String, Integer, String>() {
			

				@Override
				protected String doInBackground(String... params) {
					
					ServerConnect serverConnect = new ServerConnect();
					file = serverConnect.HTTPConnect("http://www.maadlabs.com/test/groups.php", null);
					return null;
				}
				
				protected void onPostExecute(String value)
				{
					try {
						parseJSON();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					populateGroupDisplay();
				}
		};
		groupsTask.execute(new String[1]);
 
	}
	
	public void getImages()
	{

	   AsyncTask<String, Integer, String> imagesTask;
	   
	   imagesTask = new AsyncTask<String, Integer, String>() {
		   
		   protected String doInBackground(String... params) {
			   int i = 0;
			   while(i < group.members.size())
			   {
				   imgSource = group.members.get(i);
				   ServerConnect serverConnect = new ServerConnect();
				   serverResponse = serverConnect.HTTPConnectStream("https://graph.facebook.com/"+imgSource+"/picture?type=large", null);
				   profilePicture = BitmapFactory.decodeStream(serverResponse);
				   tmpImages.add(profilePicture);
				   i++;
			   }
			   return null;
			}
		   
		   protected void onPostExecute(String params)
		   {
			  groupDescription.setText(group.info);
			  copyListToArray();
			  Log.i("sizeImages", images.length+"");
			  adapter = new GridLayoutAdapter(GroupInfoActivity.this, R.layout.griditem_layout_group_info, images);
			  groupMembers.setAdapter(adapter);
			   
		   }
			   
	   };
	 imagesTask.execute(new String[0]);		  
	}
}
