package com.maadlabs.peepsync;

import android.graphics.Bitmap;

import com.google.android.maps.GeoPoint;

public class User {
	
	private String id;
	private double lat;
	private double lon;
	private Bitmap displayPic;
	private String info;
	
	public Bitmap getDisplayPic() {
		return displayPic;
	}
	public void setDisplayPic(Bitmap displayPic) {
		this.displayPic = displayPic;
	}
	public User(int index,String name, double lat,double lon, Bitmap image, String info){
		this.id=name;
		this.lat=lat;
		this.lon=lon;
		this.displayPic=image;
		this.info=info;
	}
	public String getId() {
		return id;
	}
	public void setId(String name) {
		this.id = name;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}

}
