package com.maadlabs.peepsync;

public class CheckBoxItem {

	String groupId;
	boolean selected;
	
	CheckBoxItem(String id)
	{
		groupId = id;
		selected = false;
	}
	
	public void setSelected(boolean selected) {
	    this.selected = selected;
	  }

}

