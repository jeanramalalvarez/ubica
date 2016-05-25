package com.ubicapp.util.utiles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Jean Ramal Alvarez
 * @since 04/09/2014
 * */
public class MyAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
	
	private Context context;
	
	private ProgressDialog progressDialog;
	
	private Intent intent;
	
	private boolean agregarContexto;
	
	private boolean finalizarActividad;
	
	private boolean exito;
	
	private String mensaje;
	
	public String observacion;
	
	public Long idMotivo;
	
	public MyAsyncTask() {
	}
	
	public MyAsyncTask(Context context, Intent intent) {
		this.context = context;
		this.intent = intent;
	}
	
	public MyAsyncTask(Context context, Intent intent, boolean finalizarActividad) {
		this.context = context;
		this.intent = intent;
		this.finalizarActividad = finalizarActividad;
	}
	
	public MyAsyncTask(Context context, Intent intent, boolean finalizarActividad, boolean agregarContexto) {
		this.context = context;
		this.intent = intent;
		this.finalizarActividad = finalizarActividad;
		this.agregarContexto = agregarContexto;
	}
	
	@Override
    protected void onPreExecute(){
		progressDialog = ProgressDialog.show(context, "", "Cargando");
    }

	@Override
	protected Boolean doInBackground(Integer... params) {
		if (params == null){return false;}
		try {
			
			switch (params[0]) {
			
			case 1:
				irLogin();
				break;
			}
		} catch (Exception e) {
			Log.e("error", " Se produjo un error " + e.getMessage() + " " + e.getCause());
			return false;
		}
		return true;
	}

	@Override
    protected void onPostExecute(Boolean result){
		progressDialog.dismiss();
		
		if(result){
			if(exito){
				if(agregarContexto){
					//Util.addContext(context);
				}
				if (finalizarActividad) {
					((Activity)this.context).finish();
				}
				if(intent!=null) {
					if(mensaje != null){
						intent.putExtra("mensajeToad", mensaje);
					}
					context.startActivity(intent);
				}
			}else{
				if(mensaje != null){
					Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
				}
			}
		}else{
		   Toast.makeText(context, "Se produjo un error", Toast.LENGTH_SHORT).show();
		}

	}
	
	private void irLogin(){
		mensaje = "";
		exito = true;
	}

}