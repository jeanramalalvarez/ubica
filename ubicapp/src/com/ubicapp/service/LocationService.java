package com.ubicapp.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.ubicapp.util.Constantes;
import com.ubicapp.util.Util;

public class LocationService extends Service implements LocationListener {
	
	private static final String TAG = LocationService.class.getSimpleName();
	
	private Location location;
	
	private Location currentLocation;
	
	//private Timer timer;
	
	private Handler handler;
	
	@Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
    }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "Servicio iniciado...");
		this.ejecutarTareaProgramada();
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "Servicio destruido...");
		//timer.cancel();
		super.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void procesarLocation(){
		Log.d(TAG, "procesarLocation");
		location = Util.getLocation(this, Constantes.DISTANCIA_MIN, Constantes.TIEMPO_MIN, this);
		actualizarLocation(location, currentLocation);
	}
	
	public void actualizarLocation(Location location, Location currentLocation){
		Log.d(TAG, "actualizarLocation");

		if(location != null){
			if(Util.validarMejorUbicacion(location, currentLocation)){
				
				if(Util.validarTiempoUbicacion(location, currentLocation)){
	
					Log.d(TAG, "getLatitude: " + String.valueOf(location.getLatitude()));
					Log.d(TAG, "getLongitude: " + String.valueOf(location.getLongitude()));
	
					currentLocation = location;
	
					this.enviarLocation(location);
					
				}else{
					//this.enviarInformacion(currentLocation);
					Log.d(TAG, "validarTiempoUbicacion: false");
				}
				
			}else{
				//this.enviarInformacion(currentLocation);
				Log.d(TAG, "validarTiempoUbicacion: false");
			}
		}else{
			Log.d(TAG, "location: " + location);
		}
	}
	
	public void enviarLocation(Location location){
		Log.d(TAG, "enviarLocation");
		
		Map<String,String> parameter = new HashMap<String, String>();
		parameter.put("deImei", "" + Util.getBatteryLevel(this));
		parameter.put("nuLati", String.valueOf(location.getLatitude()));
		parameter.put("nuLong", String.valueOf(location.getLongitude()));
		parameter.put("deBate", Util.getIMEI(this));
		parameter.put("deDire", "default");
		parameter.put("feMovl", Util.getTime(null));
		parameter.put("latlng", "");
		Util.sendPost2(Constantes.URL_SERV_SEGUI, parameter);
	}
	
	public void ejecutarTareaProgramada(){
		Log.d(TAG, "ejecutarTareaProgramada");

		//timer = new Timer();
	    //timer.schedule(new RemindTask(), 0, Constants.PERIODO_ENVIO_MI);
		
        //handler = new Handler(Looper.getMainLooper()); 
        handler = new Handler(Looper.getMainLooper());
	    handler.postDelayed(new Runnable() {
	    	@Override
	    	public void run() {
	    		Log.d(TAG, "INICIO DEL PROCESO");
	    		long tiempoInicial = System.currentTimeMillis();
	    		procesarLocation();
	    		Log.d(TAG, "FIN DEL PROCESO tiempo transcurrido [" + (System.currentTimeMillis() - tiempoInicial) + " ms.]");
	    		handler.postDelayed(this, Constantes.PERIODO_ENVIO_MS);
	    	}
	    }, Constantes.PERIODO_ENVIO_MS);
	    
	}
	
	/*class RemindTask extends TimerTask {

        @SuppressLint("NewApi")
		public void run() {
        	procesarLocation();
        }
    }*/

}
