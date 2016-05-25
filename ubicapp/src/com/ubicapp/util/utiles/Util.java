package com.ubicapp.util.utiles;

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
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;
import android.util.Log;

@SuppressWarnings("unchecked")
public class Util {

	public static void sendPost2(String url, Map<String, String> parameter) {
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put("URL", url);
		sendPost2(mapa, parameter);
	}

	public static void sendPost2(Map<String, String> url,
			Map<String, String> parameter) {

		class SendPostReqAsyncTask extends
				AsyncTask<Map<String, String>, Void, String> {

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
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					con.getOutputStream());
			outputStreamWriter.write(params.toString());
			outputStreamWriter.flush();

			int responseCode = con.getResponseCode();
			Log.d("Util", "\nEnviando peticion 'POST' a la URL : " + url);
			Log.d("Util", "Parametros POST: " + params);
			Log.d("Util", "Codigo de respuesta: " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine + "\n");
			}
			in.close();

			result = response.toString();

			Log.d("Util", "Resultado POST: " + result);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	public static String getIMEI(Context context) {
		TelephonyManager mngr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mngr.getDeviceId();
		return imei;
	}

	public static float getBatteryLevel(Context context) {
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

}
