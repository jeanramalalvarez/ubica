package com.ubicapp.util.gps;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Localizador {

	public static final int RECEPTOR_SOLO_GPS = 1;
	public static final int RECEPTOR_SOLO_RED = 2;
	public static final int RECEPTOR_NINGUNO = 3;
	public static final int RECEPTOR_TODOS = 4;

	private Timer timer;
	private LocationManager lm;
	private LocalizadorManejador manejador;
	private Context context = null;

	private boolean gps_habilitado = false;
	private boolean red_habilitada = false;

	private boolean fueCancelado = false;

	public int obtenerReceptor(Context contexto) {

		int receptor = RECEPTOR_TODOS;

		if (lm == null) {
			lm = (LocationManager) contexto.getSystemService(Context.LOCATION_SERVICE);
		}

		try {
			gps_habilitado = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			red_habilitada = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_habilitado && !red_habilitada) {
			return RECEPTOR_NINGUNO;
		}

		if (!gps_habilitado && red_habilitada) {
			receptor = RECEPTOR_SOLO_RED;
		}

		if (gps_habilitado && !red_habilitada) {
			receptor = RECEPTOR_SOLO_GPS;
		}

		return receptor;
	}

	public int ObtenerLocalizacion(Context contexto, LocalizadorManejador manejador) {
		
		this.manejador = manejador;
		this.context = contexto;

		int receptor = RECEPTOR_TODOS;

		if (lm == null) {
			lm = (LocationManager) contexto.getSystemService(Context.LOCATION_SERVICE);
		}

		try {
			gps_habilitado = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			red_habilitada = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_habilitado && !red_habilitada) {
			return RECEPTOR_NINGUNO;
		}

		if (!gps_habilitado && red_habilitada) {
			receptor = RECEPTOR_SOLO_RED;
		}

		if (gps_habilitado && !red_habilitada) {
			receptor = RECEPTOR_SOLO_GPS;
		}

		if (gps_habilitado)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0F, locationListenerGps);
		if (red_habilitada)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0.0F, locationListenerNetwork);
		
		timer = new Timer();
		timer.schedule(new GetLastLocation(), 50000L);

		return receptor;

	}

	private LocationListener locationListenerGps = new LocationListener() {
		
		public void onLocationChanged(Location location) {
			timer.cancel();
			if (!fueCancelado) {
				guardarLocation(location);
				manejador.localizacionEncontrada(location);
			}
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
			context = null;
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
	};

	private LocationListener locationListenerNetwork = new LocationListener() {
		
		public void onLocationChanged(Location location) {
			timer.cancel();
			if (!fueCancelado) {
				guardarLocation(location);
				manejador.localizacionEncontrada(location);
			}
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
			context = null;

		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
	};

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			context = null;
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			if (fueCancelado) {
				return;
			}

			Location net_loc = null, gps_loc = null;
			if (gps_habilitado)
				gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (red_habilitada)
				net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			// if there are both values use the latest one
			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime())
					manejador.localizacionEncontrada(null);
				else
					manejador.localizacionEncontrada(null);
				return;
			}

			if (gps_loc != null) {
				manejador.localizacionEncontrada(null);
				return;
			}
			if (net_loc != null) {
				manejador.localizacionEncontrada(null);
				return;
			}
			manejador.localizacionEncontrada(null);
		}
	}

	public void cancelarLocalizacion() {
		fueCancelado = true;
		if (timer != null) {
			timer.cancel();
		}
		if (lm != null) {
			if (gps_habilitado) {
				if (locationListenerGps != null) {
					lm.removeUpdates(locationListenerGps);
				}
			}
			if (red_habilitada) {
				if (locationListenerNetwork != null) {
					lm.removeUpdates(locationListenerNetwork);
				}
			}

		}

	}

	private void guardarLocation(Location location) {
		if (location == null) {
			return;
		}

		if (context == null) {
			return;
		}

		// AplicacionBase appContext = (AplicacionBase)
		// context.getApplicationContext();
		//
		// appContext.guardarPreferencia(AplicacionBase.LLAVE_GEO_LATITUD,
		// String.valueOf(location.getLatitude()));
		// appContext.guardarPreferencia(AplicacionBase.LLAVE_GEO_LONGITUD,
		// String.valueOf(location.getLongitude()));
		//
		//
		// appContext.guardarPreferencia(AplicacionBase.LLAVE_GEO_FECHA,
		// String.valueOf(System.currentTimeMillis()));

	}

}
