package com.ubicapp.activity;

import com.ubicapp.R;
import com.ubicapp.service.LocationService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/***
 * 
 * @author https://developer.android.com/guide/topics/location/strategies.html
 */

public class MainActivity extends Activity {
	
	private TextView lblLatitud;
	
	private TextView lblLongitud;
	
	private TextView lblTexto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d("MainActivity", "iniciando");
		
		lblLatitud = (TextView)findViewById(R.id.lblLatitud);
		lblLongitud = (TextView)findViewById(R.id.lblLongitud);
		lblTexto = (TextView)findViewById(R.id.lblTexto);
		
		lblLatitud.setText("Cargando...");
		lblLongitud.setText("Cargando...");
		
		/*
		ServiceLocation serviceLocation = new ServiceLocation(this);
		serviceLocation.setLblLatitud(lblLatitud);
		serviceLocation.setLblLongitud(lblLongitud);
		serviceLocation.setLblTexto(lblTexto);
		serviceLocation.getLocation();
		*/
		Intent intent = new Intent(this, LocationService.class);
		this.startService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
