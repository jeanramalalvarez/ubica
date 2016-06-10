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
	
	int MINU_VALI_MEJOR_UBI = 1000 * 60 * 2;
	
	int MINU_VALI_TIEMPO_UBI = 1000 * 60 * 60;
	
	//long PERIODO_ENVIO_MI = 1000 * 2;
	
	long PERIODO_ENVIO_MS = 1000 * 60 * 30;

    long DISTANCIA_MIN = 0; // 0 metros

    long TIEMPO_MIN = 1000; // 1 segundos
    
    //String FORMATO_FECHA = "yyyy/MM/dd HH:mm:ss";
    
    String FORMATO_FECHA = "yyyy-MM-dd HH:mm:ss";
}