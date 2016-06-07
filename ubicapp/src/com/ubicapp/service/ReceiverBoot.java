package com.ubicapp.service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverBoot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		this.lanzarServices(context);
		this.lanzarActivitys(context);
	}
	
	public void lanzarServices(Context context){
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.service.LocationService");
		context.startService(serviceIntent);
	}
	
	public void lanzarActivitys(Context context){
		/*
		Intent i = new Intent(context, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		*/
	}
}
