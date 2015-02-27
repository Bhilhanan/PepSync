package com.maadlabs.peepsync;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

public class TempUserList {
	static String fox="I'm the fox who jumped over a lazy dog. It was nobody's mistake."
				+" I got angry because the dog was too lazy and was not taking "
				+"any effort to understand what I say. I know that I keep"
				+"changing what I say but the dog didn't even try.";
	static String Optimus="We are a sentinel being from a planet called Cybotron"
					+"My name is Optimus Prime,code name BigBuddha, the leader of the Autobots.";
	static String Maximus="My name is Maximus Decimus Meridius, commander of the Armies of " 
			+"the North, General of the Felix Legions and loyal servant to the " 
			+"TRUE emperor, Marcus Aurelius.";
	
	static String Bourne="Someone tell me why are the CIA trying to kill me. Besides they're not very good at it.";
	static String Shakira="El zorro de allá dice demasiado. Confía en mí, las caderas no mienten.";
	static String Naruto="Dattebayo!!";
	static String Hermione="Three turns shouuld do it.";
	static String Jackie="Hey, there. Let's do Kung fu together.";	
	

	static Bitmap image;
	public static ArrayList<User> getUserList(Context context){
		ArrayList<User> userList=new ArrayList<User>();
		LatLng userLatLng=MapActivity.lastLatLng;
		image=BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_user);
		
		userList.add(new User(1,"the_fox",userLatLng.latitude+0.009,userLatLng.longitude,image,fox));
		userList.add(new User(2,"Optimus'",userLatLng.latitude,userLatLng.longitude+0.009,image,Optimus));
		userList.add(new User(3,"gladiator",userLatLng.latitude+0.009,userLatLng.longitude+0.009,image,Maximus));
		userList.add(new User(4,"JB/DW",userLatLng.latitude+0.009,userLatLng.longitude-0.009,image,Bourne));
		userList.add(new User(5,"Shakira",userLatLng.latitude-0.009,userLatLng.longitude,image,Shakira));
		userList.add(new User(6,"Naruto",userLatLng.latitude-0.009,userLatLng.longitude-0.009,image,Naruto));
		userList.add(new User(7,"Hermione",userLatLng.latitude-0.009,userLatLng.longitude+0.009,image,Hermione));
		userList.add(new User(8,"Jackie_chan",userLatLng.latitude,userLatLng.longitude-0.009,image,Jackie));
		return userList;
	}
}
