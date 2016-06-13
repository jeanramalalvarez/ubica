package com.ubicapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ubicapp.service.LocationService;

/***
 * 
 * @author Jean Ramal Alvarez
 * @since 09/06/2016
 */

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unchecked")
public class Util {
	
	private static final String TAG = Util.class.getSimpleName();
	
	public static Date getTime(){
		Log.d(TAG, "getTime");
		
		return Calendar.getInstance().getTime();
	}
	
	public static String getTime(String formato){
		Log.d(TAG, "getTime");
		
		SimpleDateFormat sdf;
		
		if(formato == null){
			sdf = new SimpleDateFormat(Constantes.FORMATO_FECHA);
		}else{
			sdf = new SimpleDateFormat(formato);
		}
		
		return sdf.format(getTime());
	}

	public static void sendPost2(String url, Map<String, String> parameter) {
		Log.d(TAG, "sendPost2");
		
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put("URL", url);
		sendPost2(mapa, parameter);
	}

	public static void sendPost2(Map<String, String> url, Map<String, String> parameter) {
		Log.d(TAG, "sendPost2");

		class SendPostReqAsyncTask extends AsyncTask<Map<String, String>, Void, String> {

			@Override
			protected String doInBackground(Map<String, String>... params) {
				return Util.sendPost(params[0].get("URL"), params[1]);
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
			}
		}
		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(url, parameter);
	}

	public static String sendPost(String _url, Map<String, String> parameter) {
		Log.d(TAG, "sendPost");
		
		StringBuilder params = new StringBuilder("");
		String result = "";

		try {

			for (String s : parameter.keySet()) {
				params.append("&" + s + "=");
				params.append(URLEncoder.encode(parameter.get(s), "UTF-8"));
			}

			// Log.i("Util", "parametros: " + params.toString());

			String url = _url;
			URL obj = new URL(_url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			// con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "UTF-8");

			con.setDoOutput(true);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
			outputStreamWriter.write(params.toString());
			outputStreamWriter.flush();

			int responseCode = con.getResponseCode();
			Log.d(TAG, "\nEnviando peticion 'POST' a la URL : " + url);
			Log.d(TAG, "Parametros POST: " + params);
			Log.d(TAG, "Codigo de respuesta: " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine + "\n");
			}
			in.close();

			result = response.toString();

			Log.d(TAG, "Resultado POST: " + result);

		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "" + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e(TAG, "" + e.getMessage());
		} catch (ProtocolException e) {
			Log.e(TAG, "" + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "" + e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, "" + e.getMessage());
		}

		return result;

	}
	
	public static boolean isNetworkAvailable(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

	public static String getIMEI(Context context) {
		Log.d(TAG, "getIMEI");
		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		return imei;
	}

	public static float getBatteryLevel(Context context) {
		Log.d(TAG, "getBatteryLevel");
		
		float result = 0;
		Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		// Error checking that probably isn't needed but I added just in case.
		if (level == -1 || scale == -1) {
			return 50.0f;
		}
		result = ((float) level / (float) scale) * 100.0f;
		
		return result;
	}
	
	public static Location getLocation(Context context, long minTime, float minDistance, LocationListener locationListener) {
		Log.d(TAG, "getLocation");
		
		LocationManager locationManager = null;
		
		Location location = null;
		
		boolean isGPSEnabled = false;

		boolean isNetworkEnabled = false;
		
        try {
        	
            locationManager = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //if (!isGPSEnabled && !isNetworkEnabled) {
            //} else {
            if (isGPSEnabled || isNetworkEnabled) {
            	if (isGPSEnabled) {
        			Log.d(TAG, "GPS " + isGPSEnabled);
        			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);
        			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            	}
                if (isNetworkEnabled) {
                	if (location == null) {
	                	Log.d(TAG, "Network " + isNetworkEnabled);
	                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, locationListener);
	                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                	}
                }
            }else {
            	Log.d(TAG, "Network " + isNetworkEnabled);
            	Log.d(TAG, "GPS " + isGPSEnabled);
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        
        return location;
    }
	
	public static boolean validarUbicacion(Location location, Location currentBestLocation) {
		Log.d(TAG, "validarUbicacion");
		
	    if (currentBestLocation == null) {
	    	// Una nueva ubicaci�n es siempre mejor que ninguna
	        return true;
	    }
	    
	    if(location.distanceTo(currentBestLocation) <= Constantes.GET_LOCATION_DISTANCIA_MIN){
	    	return false;
	    }

	    // Compruebe si la soluci�n de la nueva ubicaci�n es m�s nueva o m�s antigua
	    long diferenciaLocation = location.getTime() - currentBestLocation.getTime();
	    boolean esNuevoLocation = diferenciaLocation >= Constantes.DIFERENCIA_LOCATION;
	    //boolean esNuevo = diferenciaLocation > 0;

	    // Si han pasado m�s de dos minutos desde la ubicaci�n actual, utilice la nueva ubicaci�n
	    // Porque el usuario probablemente se ha movido
	    if (esNuevoLocation) {
	        return true;    
	    }

	    // Comprobar si la soluci�n de la nueva ubicaci�n es m�s o menos exacta
	    /*
	    int diferenciaAccuracy = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean esMasExacta = diferenciaAccuracy > 0;
	    boolean esMenosExacta = diferenciaAccuracy < 0;
	    boolean isSignificantlyLessAccurate = diferenciaAccuracy > 200;
	    */

	    // Comprobar si la vieja y nueva ubicaci�n son del mismo proveedor
	    //boolean esMismoProveedor = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    // Determinar la calidad del lugar utilizando una combinaci�n de la puntualidad y la exactitud
	    /*
	    if (esMenosExacta) {
	        return true;
	    } else if (esNuevo && !esMasExacta) {
	        return true;
	    } else if (esNuevo && !isSignificantlyLessAccurate && esMismoProveedor) {
	        return true;
	    }
	    */
	    return false;
	}

	public static boolean validarMejorUbicacion(Location location, Location currentBestLocation) {
		Log.d(TAG, "validarMejorUbicacion");
		
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	    	// Una nueva ubicaci�n es siempre mejor que ninguna
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    // Compruebe si la soluci�n de la nueva ubicaci�n es m�s nueva o m�s antigua
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    //Date newDate = new Date(location.getTime());
	    //Date currentDate = new Date(currentBestLocation.getTime());
	    boolean isSignificantlyNewer = timeDelta >= Constantes.DIFERENCIA_LOCATION;
	    boolean isSignificantlyOlder = timeDelta < Constantes.DIFERENCIA_LOCATION;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    // Si han pasado m�s de dos minutos desde la ubicaci�n actual, utilice la nueva ubicaci�n
	    // Porque el usuario probablemente se ha movido
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    // Si la nueva ubicaci�n es m�s de dos minutos mayor, debe ser peor    
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    // Comprobar si la soluci�n de la nueva ubicaci�n es m�s o menos exacta
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    // Comprobar si la vieja y nueva ubicaci�n son del mismo proveedor
	    boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    // Determinar la calidad del lugar utilizando una combinaci�n de la puntualidad y la exactitud
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}
	
	public static boolean validarTiempoUbicacion(Location location, Location currentBestLocation) {
		Log.d(TAG, "validarTiempoUbicacion");
		
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	    	// Una nueva ubicaci�n es siempre mejor que ninguna
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    // Comprobar si la nueva ubicaci�n es m�s nueva o m�s antigua
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    
	    if(timeDelta >= Constantes.MINU_VALI_TIEMPO_UBI){
	    	return true;
	    }
	    
	    return false;
	}
	
	private static boolean isSameProvider(String provider1, String provider2) {
		Log.d(TAG, "isSameProvider");
		
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	public static void ejecutarAsincrono(Context context, String tipo, Object object){
		Log.d(TAG, "ejecutarAsincrono");
		
		class SendPostReqAsyncTask extends AsyncTask<List<Object>, Void, String> {
			
			@Override
			protected String doInBackground(List<Object>... params) {
				Context context = (Context)params[0].get(0);
				String tipo = (String)params[0].get(1);
				
				if(tipo.equals("START_SERVICE")){
					context.startService(new Intent(context, ((LocationService)params[0].get(2)).getClass()));
				}
				
				return "ok";
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
			}
			
		}
		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		List<Object> lista = new ArrayList<Object>();
		lista.add(context);
		lista.add(tipo);
		lista.add(object);
		sendPostReqAsyncTask.execute(lista);
	}
	
	public static void startService(Context context, Class<?> service){
		context.startService(new Intent(context, service));
		/*Intent intent = new Intent();
		intent.setAction("com.service.LocationService");
		context.startService(intent);
		*/
	}
	
	public static double distance(Location location, Location currentBestLocation){
		
		double lat = location.getLatitude();
		double lon = location.getLongitude();
		double clat = currentBestLocation.getLatitude();
		double clon = currentBestLocation.getLongitude();
		double temp1 = (double)((lat-clat)*(lat-clat));
		double temp2 = (double)((lon-clon)*(lon-clon));
		double distance = temp1 + temp2;
	    
	    return distance;
	}

}