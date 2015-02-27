package com.maadlabs.peepsync;

public class Connection {
	String name, interest, about, id;
	Connection(String name, String interest, String about)
	{
		this.name = name;
		this.interest = interest;
		this.about = about;
		this.id = id;
	}
	public Connection()
	{
		this.name = "";
		this.interest = "";
		this.id = "";
		this.about = "";
	}
}
