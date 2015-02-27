package com.maadlabs.peepsync;

public class ChatMessage {

	String sentMessage;
	String receivedMessage;
	
	public ChatMessage(String s, String r)
	{
		sentMessage = s;
		receivedMessage = r;
	}
	
	public ChatMessage()
	{
		sentMessage = null;
		receivedMessage = null;
	}
}
