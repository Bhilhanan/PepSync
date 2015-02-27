package com.maadlabs.peepsync;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Chat {

	String fromId, toId, textMessage;
	
	public Chat()
	{
		fromId = "";
		toId = "";
		textMessage = "";
	}
	
	public ArrayList<Chat> parseReceivedText(String data) throws Exception
	{
		ArrayList<Chat> messages = new ArrayList<Chat>();
		
		Log.i("data",data);
		JSONObject jObject = new JSONObject(data);
		
		JSONArray jMessageArray = jObject.getJSONArray("messages");
		
		Chat newMessage;
		
		for(int i = 0; i < jMessageArray.length(); i++)
		{
			newMessage = new Chat();
			JSONObject currObject = jMessageArray.getJSONObject(i);
			
			newMessage.fromId = (String) currObject.get("from");
			newMessage.textMessage = (String) currObject.getString("message");
			messages.add(newMessage);
		}
		return messages;
	}
}
