package com.maadlabs.peepsync;

import java.util.ArrayList;

public class ResultInfo {

	int type;
	
	String name, about, groups, id;
	
	String groupName;
	
	public ResultInfo(int type)
	{
		this.type = type;
		
		if(type == 1)
		{
			id = "";
			name = "";
			about = "";
			groups = "";
		}
		else
		{
			groupName = "";
		}
	}
}
