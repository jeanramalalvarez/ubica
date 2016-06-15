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
import java.net.UnknownHostException;
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
		sendPost(mapa, parameter);
	}

	public static void sendPost(Map<String, String> url, Map<String, String> parameter) {
		Log.d(TAG, "sendPost");

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
		String result = null;

		try {

			for (String s : parameter.keySet()) {
				params.append("&" + s + "=");
				params.append(URLEncoder.encode(parameter.get(s), "UTF-8"));
			}

			String url = _url;
			URL obj = new URL(_url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			Log.d(TAG, "\nEnviando peticion 'POST' a la URL : " + url);
			Log.d(TAG, "Parametros POST: " + params);

			con.setRequestMethod("POST");
			// con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "UTF-8");

			con.setDoOutput(true);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
			outputStreamWriter.write(params.toString());
			outputStreamWriter.flush();

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine + "\n");
			}
			in.close();

			result = response.toString();

			Log.d(TAG, "Codigo de respuesta: " + responseCode);
			Log.d(TAG, "Resultado POST: " + result);

		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "[UnsupportedEncodingException]" + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e(TAG, "[MalformedURLException]" + e.getMessage());
		} catch (ProtocolException e) {
			Log.e(TAG, "[ProtocolException]" + e.getMessage());
		} catch (UnknownHostException e) {
			Log.e(TAG, "[UnknownHostException]" + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "[IOException]" + e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, "[Exception]" + e.getMessage());
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

	public static int getBatteryLevel(Context context) {
		Log.d(TAG, "getBatteryLevel");

		Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int result = intent.getIntExtra("level", 0);
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
	    	// Una nueva ubicación es siempre mejor que ninguna
	        return true;
	    }
	    
	    if(location.distanceTo(currentBestLocation) <= Constantes.DISTANCIA_MINIMA){
	    	return false;
	    }

	    // Compruebe si la solución de la nueva ubicación es nueva o antigua
	    //long diferenciaLocation = location.getTime() - currentBestLocation.getTime();
	    //boolean esNuevo = diferenciaLocation > 0;

	    // Si han pasado más de dos minutos desde la ubicación actual, utilice la nueva ubicación
	    // Porque el usuario probablemente se ha movido
	    /*if (diferenciaLocation >= Constantes.DIFERENCIA_LOCATION) {
	        return true;    
	    }*/

	    // Comprobar si la solución de la nueva ubicación es más o menos exacta
	    /*
	    int diferenciaAccuracy = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean esMasExacta = diferenciaAccuracy > 0;
	    boolean esMenosExacta = diferenciaAccuracy < 0;
	    boolean isSignificantlyLessAccurate = diferenciaAccuracy > 200;
	    */

	    // Comprobar si la vieja y nueva ubicación son del mismo proveedor
	    //boolean esMismoProveedor = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    // Determinar la calidad del lugar utilizando una combinación de la puntualidad y la exactitud
	    /*
	    if (esMenosExacta) {
	        return true;
	    } else if (esNuevo && !esMasExacta) {
	        return true;
	    } else if (esNuevo && !isSignificantlyLessAccurate && esMismoProveedor) {
	        return true;
	    }
	    */
	    return true;
	}

	public static boolean isSameProvider(String provider1, String provider2) {
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