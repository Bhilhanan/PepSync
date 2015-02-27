package com.maadlabs.peepsync.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ListView;

import com.maadlabs.peepsync.MainActivity;
import com.maadlabs.peepsync.R;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> 
{
    private MainActivity mainactivity;
    private ListView groupsListT;

	public MainActivityTest() {
		super(MainActivity.class);
		// TODO Auto-generated constructor stub
	}
	protected void setUp() throws Exception {
        super.setUp();
        mainactivity = getActivity();
        groupsListT =(ListView) mainactivity.findViewById(R.id.activity_main_groups_list);
    }
	public void testMyFirstTestTextView_labelText() {
		
		final int actual = groupsListT.getCount();
	    assertEquals(1,actual);
	    
	}
}
