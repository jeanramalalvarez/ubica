package com.ubicapp.service;
import com.ubicapp.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/***
 * 
 * @author Jean Ramal Alvarez
 * @since 09/06/2016
 */

public class ReceiverBoot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		this.lanzarServices(context);
		this.lanzarActivitys(context);
	}
	
	public void lanzarServices(Context context){
		Util.startService(context, LocationService.class);
	}
	
	public void lanzarActivitys(Context context){
		/*
		Intent i = new Intent(context, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		*/
	}
}