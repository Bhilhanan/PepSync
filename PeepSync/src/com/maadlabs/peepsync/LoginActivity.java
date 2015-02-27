package com.maadlabs.peepsync;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

// login using the facebook api
public class LoginActivity extends Activity {

 private String TAG = "LoginActivity";
 Bitmap profilePicture = null;
 Handler handler;
 URL picURL;
 Intent setupIntent;
 GraphUser myUser;
 ServerConnect serverConnect;
 AsyncTask<String, Integer, String> asyncTask;
 InputStream serverResponse;
 Response myResponse;
 ChatBroadcast chatBroadcast;
 SharedPreferences sharedPreferences;
 
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_login);
  LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
  sharedPreferences = this.getSharedPreferences("com.maadlabs.peepsync", Context.MODE_PRIVATE);
 
  if(sharedPreferences.contains("interest") && sharedPreferences.contains("fbid") && (getIntent().hasExtra("from") == false))
  {
	  Intent i = new Intent(this, MainActivity.class);
	  startActivity(i);
	  finish();
  }
 
 
  authButton.setOnErrorListener(new OnErrorListener() {
   
   @Override
   public void onError(FacebookException error) {
    Log.i(TAG, "Error " + error.getMessage());
   }
   
  });

  // set permissions for a user
  authButton.setReadPermissions(Arrays.asList("basic_info","email","user_photos")); 
  
  // set  callback when session state changes in facebook
  authButton.setSessionStatusCallback(new Session.StatusCallback() {
   

   @Override
   public void call(Session session, SessionState state, Exception exception) {
    
    if (session.isOpened()) {
              Log.i(TAG,"Access Token"+ session.getAccessToken());
              Request.newMeRequest(session, new Request.GraphUserCallback() {
                  @Override
                  public void onCompleted(GraphUser user,Response response) {
                      if (user != null) { 
                       
                    	  myUser = user;
                          myResponse = response;
                          SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                        	      "com.maadlabs.peepsync", Context.MODE_PRIVATE);
                          
                          sharedPreferences.edit().putString("fbid", user.getId()).commit();
                       final String displayName = user.getFirstName() + " " + user.getLastName();
                       myUser = user;
                       try
                       {
                    	    asyncTask = new AsyncTask<String, Integer, String>() {

								@Override
								protected String doInBackground(
										String... values) {
									serverConnect = new ServerConnect();
									serverResponse = serverConnect.HTTPConnectStream("https://graph.facebook.com/"+myUser.getId()+"/picture?type=large", null);
		                    	    
									ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
									params.add(new BasicNameValuePair("firstname", myUser.getFirstName()));
									params.add(new BasicNameValuePair("lastname", myUser.getLastName()));
									params.add(new BasicNameValuePair("fbid", myUser.getId()));
									serverConnect.HTTPConnect("http://www.maadlabs.com/test/index.php", params);
									 
									profilePicture = BitmapFactory.decodeStream(serverResponse);
		                    	
								
									if(profilePicture != null)
									{
										setupIntent = new Intent(LoginActivity.this, SetupActivity.class);
										Bundle bundle = new Bundle();
										bundle.putString("display_name", displayName);
										bundle.putParcelable("display_picture", profilePicture);
										setupIntent.putExtras(bundle);
									
										startActivity(setupIntent);
									}
									else
									{
										Log.i("pp","null");
									}
									return null;
								}
                    	    };
                    	    
                    	  asyncTask.execute(new String[0]);
						} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
						}
                    		 
                      }
                  }
              }).executeAsync();
          }
    Log.i("Session State", session.getState().toString());
    
   }
  });
 }
 
 @Override
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
 }
 

}
