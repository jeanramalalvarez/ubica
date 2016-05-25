package com.ubicapp.util;

public interface Constants {
	
	String TRACK_URL = "";
	
	int TWO_MINUTES = 1000 * 60 * 2;
	
	int MINUTES = 1000 * 60 * 60;
	
	//String URL_SERV_BASE = "http://192.168.0.15:8092/ubica";
	String URL_SERV_BASE = "http://ubica.6te.net/ubica";

	String URL_SERV_SEGUI = URL_SERV_BASE + "/seguimiento.php/seguimiento";
	
    // The minimum distance to change Updates in meters
    long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters

    // The minimum time between updates in milliseconds
    long MIN_TIME_BW_UPDATES = 1000 ; // 1 second
}
