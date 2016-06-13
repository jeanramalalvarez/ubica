package com.ubicapp.util;

/***
 * 
 * @author Jean Ramal Alvarez
 * @since 09/06/2016
 */

public interface Constantes {
	
	//String URL_SERV_BASE = "http://192.168.0.23:8082/ubicaWeb";
	String URL_SERV_BASE = "http://ubicapp.6te.net";
	//String URL_SERV_BASE = "http://ubica.6te.net/ubica";
	
	//String URL_SERV_SEGUI = URL_SERV_BASE + "/seguimiento.php/seguimiento?XDEBUG_SESSION_START=ECLIPSE_DBGP&KEY=14651852772835";
	String URL_SERV_SEGUI = URL_SERV_BASE + "/seguimiento.php/seguimiento";
	
	int DIFERENCIA_LOCATION = 1000 * 50 * 1;//Milisegundos
	
	int MINU_VALI_TIEMPO_UBI = 1000 * 60 * 60;
	
	long SEND_LOCATION_RETRASO = 1000 * 1 * 20;//Milisegundos

    long GET_LOCATION_DISTANCIA_MIN = 50; //Metros

    long GET_LOCATION_TIEMPO_MIN = 1000 * 50 * 1;//Milisegundos
    
    //String FORMATO_FECHA = "yyyy/MM/dd HH:mm:ss";
    
    String FORMATO_FECHA = "yyyy-MM-dd HH:mm:ss";
}