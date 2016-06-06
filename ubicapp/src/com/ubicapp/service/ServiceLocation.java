package com.ubicapp.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ubicapp.util.Constants;
import com.ubicapp.util.utiles.Util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class ServiceLocation extends Service implements LocationListener {
	
	private final Context context;
	
	private Location location;
	
	private boolean gpsActivo;
	
	private LocationManager locationManager;
	
	private TextView lblLatitud;
	
	private TextView lblLongitud;
	
	private TextView lblTexto;
	
	private Location currentLocation;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;
    

	
	public ServiceLocation() {
		super();
		this.context = this.getApplicationContext();
	}

	public ServiceLocation(Context context) {
		super();
		this.context = context;
	}
	
	public Location getLocation () {
		Log.d("ServiceLocation", "getLocation");
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                 //updates will be send according to these arguments
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.TIEMPO_MIN, Constants.DISTANCIA_MIN, this);
                    Log.d("ServiceLocation", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.TIEMPO_MIN, Constants.DISTANCIA_MIN, this);
                        Log.d("ServiceLocation", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if (location != null) {
//                                latitude = location.getLatitude();
//                                longitude = location.getLongitude();
//                            }
                        }
                    }
                }
            }

            makeUseOfNewLocation(location);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }
	
	public void getUbicacion(){
		Log.d("ServiceLocation", "getUbicacion");
		try{
			locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
			gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(gpsActivo){
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			
			location  = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			makeUseOfNewLocation(location);
		}
	}
	
	public void makeUseOfNewLocation_(Location location){
		Log.d("ServiceLocation", "makeUseOfNewLocation");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(Calendar.getInstance().getTime());
		
		sdf = new SimpleDateFormat("ddMM");
		String deImei = sdf.format(Calendar.getInstance().getTime());
		
		lblLatitud.setText("Cargando...");
		lblLongitud.setText("Cargando...");
		lblTexto.setText(date);
		//if(isBetterLocation(location, currentLocation)){
			
			//if(validarTiempoUbicacion(location, currentLocation)){
				
				//Toast.makeText(this, "makeUseOfNewLocation", Toast.LENGTH_SHORT);
				Log.d("ServiceLocation", "getLatitude: " + String.valueOf(location.getLatitude()));
				Log.d("ServiceLocation", "getLongitude: " + String.valueOf(location.getLongitude()));
				
				lblLatitud.setText("getLatitude: " + String.valueOf(location.getLatitude()));
				lblLongitud.setText("getLongitude: " + String.valueOf(location.getLongitude()));
				lblTexto.setText(date);
				currentLocation = location;
				
				//String url = "192.168.0.15:8092/ubica/seguimiento.php/seguimiento";
				//String url = "http://ubica.6te.net/ubica/seguimiento.php/seguimiento";
				Map<String,String> parameter = new HashMap<String, String>();
				parameter.put("deImei", deImei);
				parameter.put("nuLati", String.valueOf(location.getLatitude()));
				parameter.put("nuLong", String.valueOf(location.getLongitude()));
				parameter.put("deBate", "95");
				parameter.put("deDire", "direccion");
				parameter.put("feMovl", date);
				
				Util.sendPost2(Constants.URL_SERV_SEGUI, parameter);
				
				
			/*}else{
				lblTexto.setText("validarTiempoUbicacion false: " + date);
			}*/
			
		/*}else{
			lblTexto.setText("isBetterLocation false" + date);
			//currentLocation = location;
		}*/
	}
	
	public void makeUseOfNewLocation(Location location){
		Log.d("ServiceLocation", "makeUseOfNewLocation");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(Calendar.getInstance().getTime());
		
		sdf = new SimpleDateFormat("ddMM");
		String deImei = "1-" + sdf.format(Calendar.getInstance().getTime());
		
		lblLatitud.setText("Cargando...");
		lblLongitud.setText("Cargando...");
		lblTexto.setText(date);
		if(isBetterLocation(location, currentLocation)){
			
			if(validarTiempoUbicacion(location, currentLocation)){
				
				//Toast.makeText(this, "makeUseOfNewLocation", Toast.LENGTH_SHORT);
				Log.d("ServiceLocation", "getLatitude: " + String.valueOf(location.getLatitude()));
				Log.d("ServiceLocation", "getLongitude: " + String.valueOf(location.getLongitude()));
				
				lblLatitud.setText("getLatitude: " + String.valueOf(location.getLatitude()));
				lblLongitud.setText("getLongitude: " + String.valueOf(location.getLongitude()));
				lblTexto.setText(date);
				currentLocation = location;
				
				//String url = "192.168.0.15:8092/ubica/seguimiento.php/seguimiento";
				//String url = "http://ubica.6te.net/ubica/seguimiento.php/seguimiento";
				Map<String,String> parameter = new HashMap<String, String>();
				parameter.put("deImei", deImei);
				parameter.put("nuLati", String.valueOf(location.getLatitude()));
				parameter.put("nuLong", String.valueOf(location.getLongitude()));
				parameter.put("deBate", "95");
				parameter.put("deDire", "direccion");
				parameter.put("feMovl", date);
				
				Util.sendPost2(Constants.URL_SERV_SEGUI, parameter);
				
				
			}else{
				lblTexto.setText("validarTiempoUbicacion false: " + date);
			}
			
		}else{
			lblTexto.setText("isBetterLocation false" + date);
			//currentLocation = location;
		}
	}
	
	protected boolean validarTiempoUbicacion(Location location, Location currentBestLocation) {
		Log.d("ServiceLocation", "validarTiempoUbicacion");
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	    	// Una nueva ubicación es siempre mejor que ninguna
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    // Compruebe si la solución de la nueva ubicación es más nueva o más antigua
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    
	    if(timeDelta >= Constants.MINUTES){
	    	return true;
	    }
	    
	    return false;
	}
	
	/** Determines whether one Location reading is better than the current Location fix
	 *  Determina si la lectura es un lugar mejor que el actual Ubicación fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		Log.d("ServiceLocation", "isBetterLocation");
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	    	// Una nueva ubicación es siempre mejor que ninguna
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    // Compruebe si la solución de la nueva ubicación es más nueva o más antigua
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    Date newDate = new Date(location.getTime());
	    Date currentDate = new Date(currentBestLocation.getTime());
	    boolean isSignificantlyNewer = timeDelta > Constants.TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -Constants.TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    // Si han pasado más de dos minutos desde la ubicación actual, utilice la nueva ubicación
	    // Porque el usuario probablemente se ha movido
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    // Si la nueva ubicación es más de dos minutos mayor, debe ser peor    
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    // Comprobar si la solución de la nueva ubicación es más o menos exacta
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    // Comprobar si la vieja y nueva ubicación son del mismo proveedor
	    boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    // Determinar la calidad del lugar utilizando una combinación de la puntualidad y la exactitud
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("ServiceLocation", "onStartCommand");
		getLocation();
		return START_STICKY;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Log.d("ServiceLocation", "onLocationChanged");
//		this.location = location;
//		if(location != null){
//			lblLatitud.setText("getLatitude: " + String.valueOf(location.getLatitude()));
//			lblLongitud.setText("getLongitude: " + String.valueOf(location.getLongitude()));
//		}
//		makeUseOfNewLocation(location);
		getLocation();
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
	
	public Context getContext() {
		return context;
	}
	
	public void setLblLatitud(TextView lblLatitud) {
		this.lblLatitud = lblLatitud;
	}
	
	public void setLblLongitud(TextView lblLongitud) {
		this.lblLongitud = lblLongitud;
	}
	
	public void setLblTexto(TextView lblTexto) {
		this.lblTexto = lblTexto;
	}

}
