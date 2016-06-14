package com.ubicapp.util;

/***
 * 
 * @author Jean Ramal Alvarez
 * @since 09/06/2016
 */

public interface Constantes {
	
	//String URL_SERV_BASE = "http://192.168.0.23:8082/ubicaWeb";
	String URL_SERV_BASE = "http://ubicapp.6te.net";
	
	String URL_SERV_SEGUI = URL_SERV_BASE + "/seguimiento.php/seguimiento?XDEBUG_SESSION_START=ECLIPSE_DBGP&KEY=14651852772835";
	//String URL_SERV_SEGUI = URL_SERV_BASE + "/seguimiento.php/seguimiento";
	
	long RETRASO_ENVIO = 1000 * 1 * 15;//Milisegundos

    long DISTANCIA_MINIMA = 50; //Metros

    long TIEMPO_MINIMO = 1000 * 50 * 1;//Milisegundos
    
    //String FORMATO_FECHA = "yyyy/MM/dd HH:mm:ss";
    
    String FORMATO_FECHA = "yyyy-MM-dd HH:mm:ss";
}