package com.sdi.erpreporter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;



public class ActWelcome extends Activity implements OnClickListener{

	Button bQuery, bAyarlar, bHakkinda, bServers ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_act_welcome);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    setTitle("ERP Reporter v1.00");
	    Kontroller();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.act_welcome, menu);
		return true;
	}

	
	@Override
	public void onClick(View v) {
		Toast.makeText(this, "girdi", Toast.LENGTH_SHORT);
		switch (v.getId()) {
		case R.id.btnQueryEditor:
			try {
				@SuppressWarnings("rawtypes")
				Class c = Class.forName("com.sdi.erpreporter.ActQuery");
				Intent intent = new Intent(this,c);
				startActivity(intent);
				
			} catch (ClassNotFoundException e) {
				Toast.makeText(this, "Hata : " + e.toString(), Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.btnAyar:
			try {
				@SuppressWarnings("rawtypes")
				Class c = Class.forName("com.sdi.erpreporter.ActSettings");
				Intent intent = new Intent(this,c);
				startActivity(intent);
				//Toast.makeText(this, "Girdi : " , Toast.LENGTH_SHORT).show();
				
			} catch (ClassNotFoundException e) {
				Toast.makeText(this, "Hata : " + e.toString(), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnServers:
			try {
				@SuppressWarnings("rawtypes")
				Class c = Class.forName("com.sdi.erpreporter.ActServers");
				Intent intent = new Intent(this,c);
				startActivity(intent);
				
			} catch (ClassNotFoundException e) {
				Toast.makeText(this, "Hata : " + e.toString(), Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.btnHakkinda:
			Builder builder = new Builder(this);
			builder.setTitle("ERP Report Builder")
					.setIcon(R.drawable.ic_launcher)
					.setMessage("MS SQL Server Sunucularýna baðlanarak "
							+"Sorgu çalýþtýrmanýz için tasarlanmýþtýr \n \n"
							+"Yiðit ERSOY\n"
							+"www.sdisoftware.com").show();
			break;

		}
		
	}

	
	private void Kontroller(){
		
		bQuery = (Button) findViewById(R.id.btnQueryEditor);
		bAyarlar = (Button) findViewById(R.id.btnAyar);
		bHakkinda = (Button) findViewById(R.id.btnHakkinda);
		bServers = (Button) findViewById(R.id.btnServers);
		
		bQuery.setOnClickListener(this);
		bAyarlar.setOnClickListener(this);
		bHakkinda.setOnClickListener(this);
		bServers.setOnClickListener(this);
		
	}
}
