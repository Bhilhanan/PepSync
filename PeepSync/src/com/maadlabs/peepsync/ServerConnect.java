package com.maadlabs.peepsync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;


	public class ServerConnect {
		
		HttpURLConnection urlConnection;
		String response;
		HttpURLConnection con;
		Handler myHandler;

		public String HTTPConnect(String rawURL, ArrayList<NameValuePair> params)
		{
			String op;
			op = "";
			URL url = null;
			
			if(params != null)
			{
				rawURL = rawURL + "?" + URLEncodedUtils.format(params, "utf-8");
			}
			try
			{

				url = new URL(rawURL);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(15000);
				con.setReadTimeout(10000);
				
				op = readStream(con.getInputStream());

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			return op;

		}
		
		public InputStream HTTPConnectStream(String rawURL, ArrayList<NameValuePair> params)
		{
			String op;
			op = "";
			URL url = null;
			
			if(params != null)
			{
				rawURL = rawURL + "?" + URLEncodedUtils.format(params, "utf-8");
			}
			try
			{

				url = new URL(rawURL);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(10000);
				con.setReadTimeout(5000);
				return con.getInputStream();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return null;
		}

			private String readStream(InputStream in) {
			  BufferedReader reader = null;
			  String file;
			  file = "";
			  try {
			    reader = new BufferedReader(new InputStreamReader(in));
			    response = "";
			    while ((response = reader.readLine()) != null) {
			    	file =  file + response;
			    }
			  } catch (IOException e) {
			    e.printStackTrace();
			  } finally {
			    if (reader != null) {
			      try {
			        reader.close();
			      } catch (IOException e) {
			        e.printStackTrace();
			        }
			    }
			  }
			  return file;
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
