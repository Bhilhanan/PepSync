package com.maadlabs.peepsync;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {

	ListView chatMessagesList;
	Button sendButton;
	EditText composeMessageBox;
	ServerConnect serverConnect;
	Bundle fromActivityBundle;
	CustomListAdapter<ChatMessage> messagesListAdapter;
	ArrayList<ChatMessage> chatMessages;
	BroadcastReceiver newMessageBroadcast;
	IntentFilter newMessageIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		fromActivityBundle = getIntent().getExtras();
		
		init();
		setListeners();
		if(fromActivityBundle.containsKey("data"))
			displayMessage(getIntent());
		
		if(savedInstanceState != null)
		 {
			  chatMessages = (ArrayList<ChatMessage>) savedInstanceState.getSerializable("messages");
			  messagesListAdapter = new CustomListAdapter<ChatMessage>(this, R.layout.listitem_layout_chat_message, chatMessages) {
					
					private ChatMessageHolder holder;

					public void holderInitIf()
					{
						holder = new ChatMessageHolder();
						holder.sentMessage  = (TextView) row.findViewById(R.id.textview_sent_message_content);
						holder.receivedMessage = (TextView) row.findViewById(R.id.textview_received_message_content);
						
						row.setTag(holder);
					}
					
					public void holderInitElse()
					{
						holder = (ChatMessageHolder) row.getTag();
					}
					
					public void setValues()
					{
						ChatMessage message = options.get(myPosition);
						if(message.sentMessage != null)
						{
							holder.sentMessage.setVisibility(View.VISIBLE);
							holder.sentMessage.setText(message.sentMessage);
							holder.sentMessage.setBackgroundResource(R.drawable.sent_message);
							holder.receivedMessage.setVisibility(View.GONE);
						}
						else
						{
							holder.receivedMessage.setVisibility(View.VISIBLE);
							holder.receivedMessage.setText(message.receivedMessage);
							holder.receivedMessage.setBackgroundResource(R.drawable.received_message);
							holder.sentMessage.setVisibility(View.GONE);
						}
					}
				};
				
				chatMessagesList.setAdapter(messagesListAdapter);
			  messagesListAdapter.notifyDataSetChanged();
		 }
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		  super.onSaveInstanceState(savedInstanceState);
		  // Save UI state changes to the savedInstanceState.
		  savedInstanceState.putSerializable("messages", chatMessages);
		}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		this.unregisterReceiver(newMessageBroadcast);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		messagesListAdapter.notifyDataSetChanged();
		this.registerReceiver(newMessageBroadcast, newMessageIntent);
	}

	public void init()
	{
		chatMessagesList = (ListView) findViewById(R.id.listview_messages_list);
		sendButton = (Button) findViewById(R.id.button_send_message);
		composeMessageBox = (EditText) findViewById(R.id.compose_message_box);
		chatMessages = new ArrayList<ChatMessage>();
	}
	
	public void setListeners()
	{
		sendButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {

				serverConnect = new ServerConnect();
				
				final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("from", fromActivityBundle.getString("from")));
				params.add(new BasicNameValuePair("to", fromActivityBundle.getString("to")));
				params.add(new BasicNameValuePair("message", composeMessageBox.getText().toString()));
				params.add(new BasicNameValuePair("flag", 1+""));
				
				new AsyncTask<String,Integer,String>()
				{
					
					@Override
					protected String doInBackground(String... args) {
						
						serverConnect.HTTPConnect("http://www.maadlabs.com/test/chat.php", params);
						return null;
					}
					
				}.execute(new String[0]);
				
				chatMessages.add(new ChatMessage(composeMessageBox.getText().toString(), null));
				messagesListAdapter.notifyDataSetChanged();
				
				composeMessageBox.setText("");
			}
			
		});
		
		messagesListAdapter = new CustomListAdapter<ChatMessage>(this, R.layout.listitem_layout_chat_message, chatMessages) {
			
			private ChatMessageHolder holder;

			public void holderInitIf()
			{
				holder = new ChatMessageHolder();
				holder.sentMessage  = (TextView) row.findViewById(R.id.textview_sent_message_content);
				holder.receivedMessage = (TextView) row.findViewById(R.id.textview_received_message_content);
				
				row.setTag(holder);
			}
			
			public void holderInitElse()
			{
				holder = (ChatMessageHolder) row.getTag();
			}
			
			public void setValues()
			{
				ChatMessage message = options.get(myPosition);
				if(message.sentMessage != null)
				{
					holder.sentMessage.setVisibility(View.VISIBLE);
					holder.sentMessage.setText(message.sentMessage);
					holder.sentMessage.setBackgroundResource(R.drawable.sent_message);
					holder.receivedMessage.setVisibility(View.GONE);
				}
				else
				{
					holder.receivedMessage.setVisibility(View.VISIBLE);
					holder.receivedMessage.setText(message.receivedMessage);
					holder.receivedMessage.setBackgroundResource(R.drawable.received_message);
					holder.sentMessage.setVisibility(View.GONE);
				}
			}
		};
		
		chatMessagesList.setAdapter(messagesListAdapter);

		newMessageBroadcast = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				displayMessage(intent);
			}
		
		};
		newMessageIntent = new IntentFilter("com.maadlabs.peepsync.RECEIVED_MESSAGE_TEXT");
		newMessageIntent.setPriority(2000);
		this.registerReceiver(newMessageBroadcast, newMessageIntent);
	}
	
	public void displayMessage(Intent intent)
	{
		Log.i("chatActivity", "message");
		Bundle bundle = intent.getExtras();
		ArrayList<Chat> messages = null;
		
		try {
			messages = new ArrayList<Chat>();
			messages = (new Chat()).parseReceivedText(bundle.getString("data"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		for(int i = 0; i < messages.size(); i++)
		{
			Chat message = messages.get(i);
			if(message.fromId.equals(fromActivityBundle.getString("to")))
			{
				ChatMessage newMessage = new ChatMessage();
				newMessage.receivedMessage = message.textMessage;
				chatMessages.add(newMessage);
			}
			
		}
		
		messagesListAdapter.notifyDataSetChanged();
		newMessageBroadcast.abortBroadcast();
	}
	
}
