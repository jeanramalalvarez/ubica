package com.ubicapp.util;

public interface Constants {
	
	String TRACK_URL = "";
	
	String URL_SERV_BASE = "http://192.168.0.23:8082/ubicaWeb";
	//String URL_SERV_BASE = "http://ubica.6te.net/ubica";
	
	String URL_SERV_SEGUI = URL_SERV_BASE + "/seguimiento.php/seguimiento?XDEBUG_SESSION_START=ECLIPSE_DBGP&KEY=14651852772835";
	
	int TWO_MINUTES = 1000 * 60 * 2;
	
	int MINUTES = 1000 * 60 * 60;
	
	long PERIODO_ENVIO_MI = 1000 * 2;
	
	long PERIODO_ENVIO_MS = 1000 * 5;

    long DISTANCIA_MIN = 0; // 0 meters

    long TIEMPO_MIN = 1000; // 1 second
    
    //String FORMATO_FECHA = "yyyy/MM/dd HH:mm:ss";
    
    String FORMATO_FECHA = "yyyy-MM-dd HH:mm:ss";
}
